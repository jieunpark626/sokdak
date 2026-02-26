package com.sokdak.randomchat.application.usecases

import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.application.exceptions.NotInQueueException
import com.sokdak.randomchat.application.exceptions.NotInRoomException
import com.sokdak.randomchat.domain.repositories.UserChatStatusRepository
import com.sokdak.randomchat.domain.valueobjects.UserChatStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class HandleDisconnectionUseCase(
    private val userChatStatusRepository: UserChatStatusRepository,
    private val leaveWaitingQueueUseCase: LeaveWaitingQueueUseCase,
    private val exitChatRoomUseCase: ExitChatRoomUseCase,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(userId: UserId) {
        when (userChatStatusRepository.getStatus(userId)) {
            is UserChatStatus.Waiting -> {
                try {
                    leaveWaitingQueueUseCase.execute(userId)
                } catch (e: NotInQueueException) {
                    // 이미 제거됨
                }
            }

            is UserChatStatus.InRoom -> {
                try {
                    exitChatRoomUseCase.execute(userId)
                } catch (e: NotInRoomException) {
                    // 이미 종료됨
                }
            }

            is UserChatStatus.Idle -> {
                // no-op
            }
        }
    }
}
