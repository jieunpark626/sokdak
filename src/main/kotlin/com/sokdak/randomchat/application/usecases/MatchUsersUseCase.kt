package com.sokdak.randomchat.application.usecases

import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.domain.repositories.WaitingQueueRepository
import com.sokdak.randomchat.domain.valueobjects.ChatRoomId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

data class MatchResult(
    val userA: UserId,
    val userB: UserId,
    val roomId: ChatRoomId,
)

@Service
class MatchUsersUseCase(
    private val waitingQueueRepository: WaitingQueueRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun tryMatch(): MatchResult? {
        val roomId = ChatRoomId.generate()
        val matched = waitingQueueRepository.matchPair(roomId) ?: return null

        log.info(
            "Users matched: userA={}, userB={}, roomId={}",
            matched.userA.value,
            matched.userB.value,
            roomId.value,
        )

        return MatchResult(matched.userA, matched.userB, roomId)
    }
}
