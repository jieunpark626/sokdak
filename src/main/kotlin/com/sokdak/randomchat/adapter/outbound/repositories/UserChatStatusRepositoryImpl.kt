package com.sokdak.randomchat.adapter.outbound.repositories

import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.domain.repositories.UserChatStatusRepository
import com.sokdak.randomchat.domain.valueobjects.ChatRoomId
import com.sokdak.randomchat.domain.valueobjects.UserChatStatus
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

// [redis 유저 상태 저장]
// key : {chat:user:{userId}}
// value : waiting(대기중) {roomId}(채팅중) 없음(아무것도 하지 않는 상태)
// userStatus : Waiting, InRoom(roomId), Idle
@Repository
class UserChatStatusRepositoryImpl(
    private val redisTemplate: StringRedisTemplate,
) : UserChatStatusRepository {
    companion object {
        private const val USER_STATUS_PREFIX = "chat:user:"
        private const val WAITING_STATUS = "waiting"

        // TODO: session TTL 문제 해결하기 -> 30분 지나면 끝나버림
        private val SESSION_TTL = Duration.ofMinutes(30)
    }

    override fun getStatus(userId: UserId): UserChatStatus {
        val key = "$USER_STATUS_PREFIX${userId.value}"
        val value = redisTemplate.opsForValue().get(key)

        return when {
            value == null -> UserChatStatus.Idle
            value == WAITING_STATUS -> UserChatStatus.Waiting
            else -> UserChatStatus.InRoom(ChatRoomId(value))
        }
    }

    override fun setWaiting(userId: UserId) {
        val key = "$USER_STATUS_PREFIX${userId.value}"
        redisTemplate.opsForValue().set(key, WAITING_STATUS, SESSION_TTL)
    }

    override fun clear(userId: UserId) {
        val key = "$USER_STATUS_PREFIX${userId.value}"
        redisTemplate.delete(key)
    }
}
