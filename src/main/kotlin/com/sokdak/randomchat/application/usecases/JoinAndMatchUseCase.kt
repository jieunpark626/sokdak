package com.sokdak.randomchat.application.usecases

import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.domain.services.ChatNotificationService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class JoinAndMatchUseCase(
    // TODO: usecase에서 usecase를 주입 받는 구조임
    private val joinWaitingQueueUseCase: JoinWaitingQueueUseCase,
    private val matchUsersUseCase: MatchUsersUseCase,
    private val chatNotificationService: ChatNotificationService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(userId: UserId) {
        joinWaitingQueueUseCase.execute(userId)

        // 매치 안되면 return -> 대기큐에만 들어감
        val result = matchUsersUseCase.tryMatch() ?: return

        chatNotificationService.notifyMatched(result.userA, result.roomId)
        chatNotificationService.notifyMatched(result.userB, result.roomId)

        log.info("Match notification sent: roomId={}", result.roomId.value)
    }
}
