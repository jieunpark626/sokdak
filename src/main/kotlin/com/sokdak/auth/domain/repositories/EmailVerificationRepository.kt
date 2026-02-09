package com.sokdak.auth.domain.repositories

import com.sokdak.auth.domain.entities.EmailVerification
import com.sokdak.common.domain.valueobjects.UserId

interface EmailVerificationRepository {
    fun save(verification: EmailVerification): EmailVerification

    fun findByToken(token: String): EmailVerification?

    fun findByUserId(userId: UserId): List<EmailVerification>

    fun deleteByUserId(userId: UserId)

    fun deleteExpiredTokens()

    fun deleteVerifiedTokens()
}
