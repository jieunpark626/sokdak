package com.sokdak.auth.domain.entities

import com.sokdak.common.domain.valueobjects.UserId
import de.huxhorn.sulky.ulid.ULID
import java.time.Instant

class RefreshToken private constructor(
    val id: String,
    val userId: UserId,
    val token: String,
    val expiresAt: Instant,
    val createdAt: Instant,
) {
    companion object {
        private val ulid = ULID()

        fun create(
            userId: UserId,
            token: String,
            expiresAt: Instant,
        ): RefreshToken {
            val now = Instant.now()

            return RefreshToken(
                id = ulid.nextULID(),
                userId = userId,
                token = token,
                expiresAt = expiresAt,
                createdAt = now,
            )
        }

        fun restore(
            id: String,
            userId: UserId,
            token: String,
            expiresAt: Instant,
            createdAt: Instant,
        ): RefreshToken {
            return RefreshToken(
                id = id,
                userId = userId,
                token = token,
                expiresAt = expiresAt,
                createdAt = createdAt,
            )
        }
    }

    fun isExpired(): Boolean {
        return Instant.now().isAfter(expiresAt)
    }
}
