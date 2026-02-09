package com.sokdak.randomchat.adapter.outbound.repositories

import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.domain.entities.ChatRoom
import com.sokdak.randomchat.domain.enums.ChatStatus
import com.sokdak.randomchat.domain.repositories.ChatRoomRepository
import com.sokdak.randomchat.domain.valueobjects.ChatRoomId
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Instant

/*
    [redis 채팅방 저장]
    key = chat:room:{roomId}
    value = {userA, userB, status, createdAt} (ChatRoom?)
*/
@Repository
class ChatRoomRepositoryImpl(
    private val redisTemplate: StringRedisTemplate,
) : ChatRoomRepository {
    companion object {
        private const val ROOM_PREFIX = "chat:room:"
    }

    override fun findById(roomId: ChatRoomId): ChatRoom? {
        val key = "$ROOM_PREFIX${roomId.value}"
        val data = redisTemplate.opsForHash<String, String>().entries(key)

        if (data.isEmpty()) return null

        return ChatRoom(
            id = roomId,
            userA = UserId(data["userA"]!!),
            userB = UserId(data["userB"]!!),
            status = ChatStatus.valueOf(data["status"]!!),
            createdAt = Instant.ofEpochMilli(data["createdAt"]!!.toLong()),
        )
    }

    override fun close(roomId: ChatRoomId) {
        val key = "$ROOM_PREFIX${roomId.value}"
        redisTemplate.delete(key)
    }
}
