package com.sokdak.auth.adapter.inbound.api.dto.requests

import jakarta.validation.constraints.NotBlank

// TODO: delete
data class VerifyEmailRequest(
    @field:NotBlank(message = "Token is required")
    val token: String,
)
