package com.sokdak.auth.domain.entities

import com.sokdak.auth.domain.valueobjects.Email
import com.sokdak.common.domain.valueobjects.UserId
import de.huxhorn.sulky.ulid.ULID
import java.security.SecureRandom
import java.time.Instant
import java.util.Base64

class EmailVerification private constructor(
    val id: String,
    val userId: UserId,
    val email: Email,
    val token: String,
    val expiresAt: Instant,
    val createdAt: Instant,
    var verifiedAt: Instant?,
) {
    companion object {
        private val ulid = ULID()
        private val secureRandom = SecureRandom()
        private const val TOKEN_LENGTH = 32

        fun create(
            userId: UserId,
            email: Email,
            expiresAt: Instant,
        ): EmailVerification {
            val now = Instant.now()
            val token = generateSecureToken()

            return EmailVerification(
                id = ulid.nextULID(),
                userId = userId,
                email = email,
                token = token,
                expiresAt = expiresAt,
                createdAt = now,
                verifiedAt = null,
            )
        }

        fun restore(
            id: String,
            userId: UserId,
            email: Email,
            token: String,
            expiresAt: Instant,
            createdAt: Instant,
            verifiedAt: Instant?,
        ): EmailVerification {
            return EmailVerification(
                id = id,
                userId = userId,
                email = email,
                token = token,
                expiresAt = expiresAt,
                createdAt = createdAt,
                verifiedAt = verifiedAt,
            )
        }

        private fun generateSecureToken(): String {
            val tokenBytes = ByteArray(TOKEN_LENGTH)
            secureRandom.nextBytes(tokenBytes)
            return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes)
        }
    }

    fun isExpired(): Boolean {
        return Instant.now().isAfter(expiresAt)
    }

    fun isVerified(): Boolean {
        return verifiedAt != null
    }

    fun verify() {
        require(!isExpired()) { "Token has expired" }
        require(!isVerified()) { "Token already verified" }
        verifiedAt = Instant.now()
    }
}
