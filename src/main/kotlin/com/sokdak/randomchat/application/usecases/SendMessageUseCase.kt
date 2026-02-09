package com.sokdak.randomchat.application.usecases

import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.application.exceptions.InvalidMessageException
import com.sokdak.randomchat.application.exceptions.MessageTooLongException
import com.sokdak.randomchat.application.exceptions.NotInRoomException
import com.sokdak.randomchat.domain.repositories.ChatRoomRepository
import com.sokdak.randomchat.domain.repositories.UserChatStatusRepository
import com.sokdak.randomchat.domain.services.ChatNotificationService
import com.sokdak.randomchat.domain.valueobjects.UserChatStatus
import de.huxhorn.sulky.ulid.ULID
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SendMessageUseCase(
    private val userChatStatusRepository: UserChatStatusRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val chatNotificationService: ChatNotificationService,
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val ulid = ULID()

    companion object {
        private const val MAX_MESSAGE_LENGTH = 1000
    }

    fun execute(
        userId: UserId,
        content: String,
    ) {
        if (content.isBlank()) {
            throw InvalidMessageException()
        }
        if (content.length > MAX_MESSAGE_LENGTH) {
            throw MessageTooLongException()
        }

        // 1. 현재 유저가 채팅방에 속해있는 상태인지 확인
        // 2. 채팅방에 속해있다면, roomId 추출 -> status 에 있음
        val status = userChatStatusRepository.getStatus(userId)
        if (status !is UserChatStatus.InRoom) throw NotInRoomException()
        val roomId = status.roomId

        // 3. 같은 방에 속해있는 partnerId 추출
        val room = chatRoomRepository.findById(roomId) ?: throw NotInRoomException()
        val partnerId = room.getPartner(userId)

        // 4. 메시지 전달
        val messageId = ulid.nextULID()
        chatNotificationService.notifyMessageReceived(partnerId, messageId, content)

        log.debug(
            "Message sent: userId={}, roomId={}, messageId={}",
            userId.value,
            roomId.value,
            messageId,
        )
    }
}
