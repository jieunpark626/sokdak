package com.sokdak.auth.adapter.outbound.repositories

import com.sokdak.auth.adapter.outbound.entities.EmailVerificationJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.Instant

interface EmailVerificationJpaRepository : JpaRepository<EmailVerificationJpaEntity, String> {
    fun findByToken(token: String): EmailVerificationJpaEntity?

    fun findByUserId(userId: String): List<EmailVerificationJpaEntity>

    fun deleteByUserId(userId: String)

    @Modifying
    @Query("DELETE FROM EmailVerificationJpaEntity e WHERE e.expiresAt < :now")
    fun deleteExpiredTokens(now: Instant)

    @Modifying
    @Query("DELETE FROM EmailVerificationJpaEntity e WHERE e.verifiedAt IS NOT NULL")
    fun deleteVerifiedTokens()
}
