package com.sokdak.randomchat.adapter.outbound.repositories

import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.randomchat.domain.repositories.MatchedPair
import com.sokdak.randomchat.domain.repositories.WaitingQueueRepository
import com.sokdak.randomchat.domain.valueobjects.ChatRoomId
import jakarta.annotation.PostConstruct
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.stereotype.Repository

@Repository
class WaitingQueueRepositoryImpl(
    private val redisTemplate: StringRedisTemplate,
) : WaitingQueueRepository {
    private lateinit var matchScript: DefaultRedisScript<List<*>>

    companion object {
        private const val WAITING_QUEUE = "chat:waiting"
        private const val USER_PREFIX = "chat:user:"
        private const val ROOM_PREFIX = "chat:room:"
    }

    @PostConstruct
    fun init() {
        matchScript =
            DefaultRedisScript<List<*>>().apply {
                setLocation(ClassPathResource("redis/match_users.lua"))
                setResultType(List::class.java)
            }
    }

    override fun joinQueue(userId: UserId) {
        val score = System.currentTimeMillis().toDouble()
        redisTemplate.opsForZSet().add(WAITING_QUEUE, userId.value, score)
    }

    override fun leaveQueue(userId: UserId) {
        redisTemplate.opsForZSet().remove(WAITING_QUEUE, userId.value)
    }

    override fun matchPair(roomId: ChatRoomId): MatchedPair? {
        val now = System.currentTimeMillis().toString()

        val result =
            redisTemplate.execute(
                matchScript,
                listOf(WAITING_QUEUE),
                USER_PREFIX,
                ROOM_PREFIX,
                roomId.value,
                now,
            )

        if (result.isEmpty() || result.size < 3 || result.any { it == null }) {
            return null
        }

        return MatchedPair(
            userA = UserId(result[0].toString()),
            userB = UserId(result[1].toString()),
        )
    }
}
