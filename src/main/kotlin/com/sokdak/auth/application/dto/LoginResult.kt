package com.sokdak.auth.application.dto

data class LoginResult(
    val user: UserDto,
    val tokens: AuthTokenDto,
)