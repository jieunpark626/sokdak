package com.sokdak.auth.adapter.outbound.repositories

import com.sokdak.auth.adapter.outbound.entities.RefreshTokenJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.Instant

interface RefreshTokenJpaRepository : JpaRepository<RefreshTokenJpaEntity, String> {
    fun findByToken(token: String): RefreshTokenJpaEntity?

    fun findByUserId(userId: String): List<RefreshTokenJpaEntity>

    fun deleteByToken(token: String)

    fun deleteByUserId(userId: String)

    @Modifying
    @Query("DELETE FROM RefreshTokenJpaEntity r WHERE r.expiresAt < :now")
    fun deleteExpiredTokens(now: Instant)
}
