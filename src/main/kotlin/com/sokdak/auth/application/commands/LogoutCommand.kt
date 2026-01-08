package com.sokdak.auth.application.commands

data class LogoutCommand(
    val refreshToken: String,
)
