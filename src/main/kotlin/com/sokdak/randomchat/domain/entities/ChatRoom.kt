package com.sokdak.randomchat.domain.entities

import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.domain.enums.ChatStatus
import com.sokdak.randomchat.domain.valueobjects.ChatRoomId
import java.time.Instant

data class ChatRoom(
    val id: ChatRoomId,
    val userA: UserId,
    val userB: UserId,
    val status: ChatStatus,
    val createdAt: Instant,
) {
    fun containsUser(userId: UserId): Boolean = userA == userId || userB == userId

    fun getPartner(userId: UserId): UserId = if (userA == userId) userB else userA

    fun close(): ChatRoom = copy(status = ChatStatus.CLOSED)
}
