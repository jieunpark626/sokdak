// auth/adapter/inbound/api/dto/request/RegisterUserRequest.kt
package com.sokdak.auth.adapter.inbound.api.dto.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size


data class RegisterUserRequest(
    @field:NotBlank(message = "Login ID is required")
    @field:Size(min = 4, max = 50, message = "Login ID must be between 4 and 50 characters")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9_]+$",
        message = "Login ID must contain only alphanumeric characters and underscores",
    )
    val loginId: String,
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,
    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters")
    val password: String,
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    val name: String,
    @field:NotBlank(message = "Gender is required")
    @field:Pattern(
        regexp = "MALE|FEMALE|OTHER",
        message = "Gender must be MALE, FEMALE, or OTHER",
    )
    val gender: String,
)
