package com.sokdak.randomchat.application.usecases

import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.application.exceptions.NotInRoomException
import com.sokdak.randomchat.domain.repositories.ChatRoomRepository
import com.sokdak.randomchat.domain.repositories.UserChatStatusRepository
import com.sokdak.randomchat.domain.services.ChatNotificationService
import com.sokdak.randomchat.domain.valueobjects.UserChatStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/*
    현재 채팅방 나가기
    채팅중이면 대기큐에서는 이미 나간거임 (매칭될 때 대기 큐에서 빼내줌)
    0. 현재 나가려는 유저의 상태를 검증
    1. 현재 속한 채팅방 찾기
    2. 파트너 찾기
    3. 유저 상태 삭제 (redis: chat:user:{userId} 삭제)
    4. 파트너 유저 상태 삭제 (redis: chat:user:{userId} 삭제)
    5. 채팅방 삭제 (redis: chat:room:{roomId})
    6. 파트너에게 상대방이 나감을 알려줌
*/
@Service
class ExitChatRoomUseCase(
    private val userChatStatusRepository: UserChatStatusRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val chatNotificationService: ChatNotificationService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(userId: UserId) {
        val status = userChatStatusRepository.getStatus(userId)
        val roomId =
            when (status) {
                is UserChatStatus.InRoom -> status.roomId
                else -> throw NotInRoomException()
            }

        val room = chatRoomRepository.findById(roomId) ?: throw NotInRoomException()
        val partnerId = room.getPartner(userId)

        userChatStatusRepository.clear(userId)
        userChatStatusRepository.clear(partnerId)
        chatRoomRepository.close(roomId)

        chatNotificationService.notifyPartnerLeft(partnerId)

        log.info("Chat room closed: roomId={}, exitedBy={}", roomId.value, userId.value)
    }
}
