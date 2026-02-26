package com.sokdak.randomchat.domain.repositories

import com.sokdak.randomchat.domain.entities.ChatRoom
import com.sokdak.randomchat.domain.valueobjects.ChatRoomId

interface ChatRoomRepository {
    fun findById(roomId: ChatRoomId): ChatRoom?

    fun close(roomId: ChatRoomId)
}
