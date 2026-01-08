package com.sokdak.auth.adapter.inbound.api.dto.requests

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "Login ID is required")
    val loginId: String,
    @field:NotBlank(message = "Password is required")
    val password: String,
)
