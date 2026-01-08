package com.sokdak.limit.adapter.outbound.entities

import com.sokdak.limit.domain.enums.ActionType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(
    name = "user_usage_limits",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["user_id", "action"]),
    ],
)
class UserUsageLimitJpaEntity(
    @Id
    @Column(name = "id", nullable = false)
    val id: String,
    @Column(name = "user_id", nullable = false)
    val userId: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    val action: ActionType,
    @Column(name = "daily_limit", nullable = false)
    val dailyLimit: Int,
    @Column(name = "daily_used", nullable = false)
    var dailyUsed: Int,
    @Column(name = "last_reset_date", nullable = false)
    var lastResetDate: LocalDate,
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant,
)
