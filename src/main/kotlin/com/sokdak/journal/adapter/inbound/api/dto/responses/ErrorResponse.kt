package com.sokdak.journal.adapter.inbound.api.dto.responses

import java.time.Instant

data class ErrorResponse(
    val timestamp: Instant = Instant.now(),
    val code: String,
    val message: String,
)