package com.sokdak.limit.adapter.outbound.mappers

import com.sokdak.limit.adapter.outbound.entities.UserUsageLimitJpaEntity
import com.sokdak.limit.domain.entities.UserUsageLimit

fun UserUsageLimitJpaEntity.toDomain(): UserUsageLimit =
    UserUsageLimit(
        id = id,
        userId = userId,
        action = action,
        dailyLimit = dailyLimit,
        dailyUsed = dailyUsed,
        lastResetDate = lastResetDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

fun UserUsageLimit.toJpaEntity(): UserUsageLimitJpaEntity =
    UserUsageLimitJpaEntity(
        id = id,
        userId = userId,
        action = action,
        dailyLimit = dailyLimit,
        dailyUsed = dailyUsed,
        lastResetDate = lastResetDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
