package com.sokdak.auth.adapter.outbound.repositories

import com.sokdak.auth.adapter.outbound.mappers.EmailVerificationMapper
import com.sokdak.auth.domain.entities.EmailVerification
import com.sokdak.auth.domain.repositories.EmailVerificationRepository
import com.sokdak.common.domain.valueobjects.UserId
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Repository
class EmailVerificationRepositoryImpl(
    private val jpaRepository: EmailVerificationJpaRepository,
    private val mapper: EmailVerificationMapper,
) : EmailVerificationRepository {
    override fun save(verification: EmailVerification): EmailVerification {
        val entity = mapper.toJpaEntity(verification)
        val saved = jpaRepository.save(entity)
        return mapper.toDomain(saved)
    }

    override fun findByToken(token: String): EmailVerification? {
        return jpaRepository.findByToken(token)
            ?.let { mapper.toDomain(it) }
    }

    override fun findByUserId(userId: UserId): List<EmailVerification> {
        return jpaRepository.findByUserId(userId.value)
            .map { mapper.toDomain(it) }
    }

    @Transactional
    override fun deleteByUserId(userId: UserId) {
        jpaRepository.deleteByUserId(userId.value)
    }

    @Transactional
    override fun deleteExpiredTokens() {
        jpaRepository.deleteExpiredTokens(Instant.now())
    }

    @Transactional
    override fun deleteVerifiedTokens() {
        jpaRepository.deleteVerifiedTokens()
    }
}
