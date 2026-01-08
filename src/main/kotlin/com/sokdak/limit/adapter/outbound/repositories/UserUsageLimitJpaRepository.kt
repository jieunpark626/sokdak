package com.sokdak.limit.adapter.outbound.repositories

import com.sokdak.limit.adapter.outbound.entities.UserUsageLimitJpaEntity
import com.sokdak.limit.domain.enums.ActionType
import org.springframework.data.jpa.repository.JpaRepository

interface UserUsageLimitJpaRepository : JpaRepository<UserUsageLimitJpaEntity, String> {
    fun findByUserIdAndAction(
        userId: String,
        action: ActionType,
    ): UserUsageLimitJpaEntity?

    fun findAllByUserId(userId: String): List<UserUsageLimitJpaEntity>
}
