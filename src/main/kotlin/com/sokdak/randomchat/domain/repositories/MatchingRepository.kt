package com.sokdak.randomchat.domain.repositories

import com.sokdak.common.domain.valueobjects.UserId

interface MatchingRepository {
    fun joinQueue(userId: UserId)

    fun leaveQueue(userId: UserId)

    fun isInQueue(userId: UserId): Boolean

    fun getQueuePosition(userId: UserId): Int?

    fun getQueueSize(): Long
}
