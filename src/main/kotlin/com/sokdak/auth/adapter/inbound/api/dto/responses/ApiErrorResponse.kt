package com.sokdak.auth.adapter.inbound.api.dto.responses

import java.time.Instant

data class ApiErrorResponse(
    val timestamp: Instant = Instant.now(),
    val code: String,
    val message: String,
)
