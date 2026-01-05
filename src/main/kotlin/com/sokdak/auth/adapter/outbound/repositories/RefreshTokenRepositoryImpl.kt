package com.sokdak.auth.adapter.outbound.repositories

import com.sokdak.auth.adapter.outbound.mappers.RefreshTokenMapper
import com.sokdak.auth.domain.entities.RefreshToken
import com.sokdak.auth.domain.repositories.RefreshTokenRepository
import com.sokdak.auth.domain.valueobjects.UserId
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Repository
class RefreshTokenRepositoryImpl(
    private val jpaRepository: RefreshTokenJpaRepository,
    private val mapper: RefreshTokenMapper,
) : RefreshTokenRepository {
    override fun save(refreshToken: RefreshToken): RefreshToken {
        val entity = mapper.toJpaEntity(refreshToken)
        val saved = jpaRepository.save(entity)
        return mapper.toDomain(saved)
    }

    override fun findByToken(token: String): RefreshToken? {
        return jpaRepository.findByToken(token)
            ?.let { mapper.toDomain(it) }
    }

    override fun findByUserId(userId: UserId): List<RefreshToken> {
        return jpaRepository.findByUserId(userId.value)
            .map { mapper.toDomain(it) }
    }

    @Transactional
    override fun deleteByToken(token: String) {
        jpaRepository.deleteByToken(token)
    }

    @Transactional
    override fun deleteByUserId(userId: UserId) {
        jpaRepository.deleteByUserId(userId.value)
    }

    @Transactional
    override fun deleteExpiredTokens() {
        jpaRepository.deleteExpiredTokens(Instant.now())
    }
}
