package com.sokdak.randomchat.domain.repositories

import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.domain.valueobjects.UserChatStatus

interface UserChatStatusRepository {
    fun getStatus(userId: UserId): UserChatStatus

    fun setWaiting(userId: UserId)

    fun clear(userId: UserId)
}
