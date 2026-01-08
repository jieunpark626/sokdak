package com.sokdak.limit.domain.services

import com.sokdak.auth.domain.enums.Plan
import com.sokdak.limit.domain.enums.ActionType

data class LimitConfig(
    val plan: Plan,
    val action: ActionType,
    val dailyLimit: Int,
)

object PlanLimitConfig {
    private val limits =
        listOf(
            LimitConfig(Plan.FREE, ActionType.AI_CHAT, 10),
            LimitConfig(Plan.FREE, ActionType.CHARACTER_CREATE, 4),
            LimitConfig(Plan.PAID, ActionType.AI_CHAT, 1000),
            LimitConfig(Plan.PAID, ActionType.CHARACTER_CREATE, 50),
        )

    fun getLimit(
        plan: Plan,
        action: ActionType,
    ): Int = limits.find { it.plan == plan && it.action == action }?.dailyLimit ?: 0
}
