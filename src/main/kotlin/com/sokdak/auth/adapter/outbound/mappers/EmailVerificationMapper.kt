package com.sokdak.auth.adapter.outbound.mappers

import com.sokdak.auth.adapter.outbound.entities.EmailVerificationJpaEntity
import com.sokdak.auth.domain.entities.EmailVerification
import com.sokdak.auth.domain.valueobjects.Email
import com.sokdak.common.domain.valueobjects.UserId
import org.springframework.stereotype.Component

@Component
class EmailVerificationMapper {
    fun toDomain(entity: EmailVerificationJpaEntity): EmailVerification {
        return EmailVerification.restore(
            id = entity.id,
            userId = UserId(entity.userId),
            email = Email(entity.email),
            token = entity.token,
            expiresAt = entity.expiresAt,
            createdAt = entity.createdAt,
            verifiedAt = entity.verifiedAt,
        )
    }

    fun toJpaEntity(domain: EmailVerification): EmailVerificationJpaEntity {
        return EmailVerificationJpaEntity(
            id = domain.id,
            userId = domain.userId.value,
            email = domain.email.value,
            token = domain.token,
            expiresAt = domain.expiresAt,
            createdAt = domain.createdAt,
            verifiedAt = domain.verifiedAt,
        )
    }
}
