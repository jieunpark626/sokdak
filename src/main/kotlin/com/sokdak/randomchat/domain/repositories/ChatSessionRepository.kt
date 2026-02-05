package com.sokdak.randomchat.domain.repositories

import com.sokdak.randomchat.domain.valueobjects.ChatRoomId
import com.sokdak.randomchat.domain.valueobjects.UserChatStatus
import com.sokdak.user.domain.valueobjects.UserId

interface ChatSessionRepository {
    fun getStatus(userId: UserId): UserChatStatus

    fun setWaiting(userId: UserId)

    fun setInRoom(
        userId: UserId,
        roomId: ChatRoomId,
    )

    fun clear(userId: UserId)
}
