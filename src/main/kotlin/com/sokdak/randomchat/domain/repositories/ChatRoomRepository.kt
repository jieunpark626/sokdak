package com.sokdak.randomchat.domain.repositories

import com.sokdak.randomchat.domain.entities.ChatRoom
import com.sokdak.randomchat.domain.valueobjects.ChatRoomId
import com.sokdak.user.domain.valueobjects.UserId

interface ChatRoomRepository {
    fun create(
        roomId: ChatRoomId,
        userA: UserId,
        userB: UserId,
    ): ChatRoom

    fun findById(roomId: ChatRoomId): ChatRoom?

    fun close(roomId: ChatRoomId)
}
