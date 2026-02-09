package com.sokdak.randomchat.application.usecases

import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.application.exceptions.AlreadyInQueueException
import com.sokdak.randomchat.application.exceptions.AlreadyInRoomException
import com.sokdak.randomchat.domain.repositories.UserChatStatusRepository
import com.sokdak.randomchat.domain.repositories.WaitingQueueRepository
import com.sokdak.randomchat.domain.valueobjects.UserChatStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

// 대기 큐 입장
// 1. 이미 큐에 존재하는지 확인
// 2. redis : 큐에 합류 시킴 ({chat:waiting} 에 유저 넣기)
// 3. redis : 유저 상태 추가하기 ({chat:user:{userId} 에 waiting 으로 추가)
@Service
class JoinWaitingQueueUseCase(
    private val userChatStatusRepository: UserChatStatusRepository,
    private val waitingQueueRepository: WaitingQueueRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(userId: UserId) {
        when (userChatStatusRepository.getStatus(userId)) {
            is UserChatStatus.Waiting -> throw AlreadyInQueueException()
            is UserChatStatus.InRoom -> throw AlreadyInRoomException()
            is UserChatStatus.Idle -> { // 진행
            }
        }

        waitingQueueRepository.joinQueue(userId)
        userChatStatusRepository.setWaiting(userId)

        log.info("User joined queue: userId={}", userId.value)
    }
}
