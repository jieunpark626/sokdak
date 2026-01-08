package com.sokdak.limit.adapter.inbound.api.dto.responses

import com.sokdak.limit.domain.enums.ActionType
import java.time.LocalDate

data class UserUsageLimitResponse(
    val userId: String,
    val action: ActionType,
    val dailyLimit: Int,
    val dailyUsed: Int,
    val remaining: Int,
    val lastResetDate: LocalDate,
)
