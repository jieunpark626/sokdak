package com.sokdak.auth.adapter.inbound.api.dto.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class ResendVerificationEmailRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,
)
