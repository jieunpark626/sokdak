package com.sokdak.randomchat.domain.repositories

import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.domain.valueobjects.ChatRoomId
import com.sokdak.randomchat.domain.valueobjects.UserChatStatus

interface ChatSessionRepository {
    fun getStatus(userId: UserId): UserChatStatus

    fun setWaiting(userId: UserId)

    fun setInRoom(
        userId: UserId,
        roomId: ChatRoomId,
    )

    fun clear(userId: UserId)
}
