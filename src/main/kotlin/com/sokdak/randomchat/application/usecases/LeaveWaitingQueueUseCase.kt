package com.sokdak.randomchat.application.usecases

import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.application.exceptions.NotInQueueException
import com.sokdak.randomchat.domain.repositories.UserChatStatusRepository
import com.sokdak.randomchat.domain.repositories.WaitingQueueRepository
import com.sokdak.randomchat.domain.valueobjects.UserChatStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/*
    사용자가 대기 큐를 나감
    1. 사용자가 대기 큐를 나갈 수 있는 상황인지 확인 : 대기 큐에 들어와서 매칭되기 전임
    - 대기큐에 들어온 적 없다면 -> 나갈 수도 없음
    - 채팅중이면 대기큐에서 빠졌을 것임 ( script 에서 빼줌 )
    2. 대기 큐에서 삭제 (redis: chat:waiting 에서 user삭제)
    3. 유저 상태 삭제 (redis: chat:user:{userId} 삭제)
 */
@Service
class LeaveWaitingQueueUseCase(
    private val userChatStatusRepository: UserChatStatusRepository,
    private val waitingQueueRepository: WaitingQueueRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(userId: UserId) {
        when (userChatStatusRepository.getStatus(userId)) {
            is UserChatStatus.Waiting -> { // 진행
            }

            else -> throw NotInQueueException() // roomId : 채팅중 (대기큐에 없음), null:
        }

        waitingQueueRepository.leaveQueue(userId)
        userChatStatusRepository.clear(userId)

        log.info("User left queue: userId={}", userId.value)
    }
}
