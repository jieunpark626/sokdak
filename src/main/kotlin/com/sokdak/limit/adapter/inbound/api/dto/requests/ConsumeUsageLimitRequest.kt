package com.sokdak.limit.adapter.inbound.api.dto.requests

import com.sokdak.limit.domain.enums.ActionType

data class ConsumeUsageLimitRequest(
    val action: ActionType,
    val count: Int = 1,
)
