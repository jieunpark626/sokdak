package com.sokdak.randomchat.domain.repositories

import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.domain.valueobjects.ChatRoomId

data class MatchedPair(
    val userA: UserId,
    val userB: UserId,
)

interface WaitingQueueRepository {
    fun joinQueue(userId: UserId)

    fun leaveQueue(userId: UserId)

    fun matchPair(roomId: ChatRoomId): MatchedPair?
}
