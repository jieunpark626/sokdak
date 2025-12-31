package com.sokdak.auth.application.commands

class RegisterUserCommand(
    val loginId: String,
    val email: String,
    val password: String,
    val name: String,
    val gender: String,
)
