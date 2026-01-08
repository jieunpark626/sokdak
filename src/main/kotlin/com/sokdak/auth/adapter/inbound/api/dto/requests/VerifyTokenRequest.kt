package com.sokdak.auth.adapter.inbound.api.dto.requests

import jakarta.validation.constraints.NotBlank

data class VerifyTokenRequest(
    @field:NotBlank(message = "Token is required")
    val token: String,
)
