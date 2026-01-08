package com.sokdak.auth.adapter.outbound.mappers

import com.sokdak.auth.adapter.outbound.entities.RefreshTokenJpaEntity
import com.sokdak.auth.domain.entities.RefreshToken
import com.sokdak.auth.domain.valueobjects.UserId
import org.springframework.stereotype.Component

@Component
class RefreshTokenMapper {
    fun toDomain(entity: RefreshTokenJpaEntity): RefreshToken {
        return RefreshToken.restore(
            id = entity.id,
            userId = UserId(entity.userId),
            token = entity.token,
            expiresAt = entity.expiresAt,
            createdAt = entity.createdAt,
        )
    }

    fun toJpaEntity(domain: RefreshToken): RefreshTokenJpaEntity {
        return RefreshTokenJpaEntity(
            id = domain.id,
            userId = domain.userId.value,
            token = domain.token,
            expiresAt = domain.expiresAt,
            createdAt = domain.createdAt,
        )
    }
}
