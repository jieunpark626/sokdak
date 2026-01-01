package com.sokdak.auth.adapter.inbound.api.mappers

import com.sokdak.auth.adapter.inbound.api.dto.requests.LoginRequest
import com.sokdak.auth.adapter.inbound.api.dto.requests.RegisterUserRequest
import com.sokdak.auth.adapter.inbound.api.dto.responses.LoginResponse
import com.sokdak.auth.adapter.inbound.api.dto.responses.LoginUserResponse
import com.sokdak.auth.adapter.inbound.api.dto.responses.TokenResponse
import com.sokdak.auth.adapter.inbound.api.dto.responses.UserResponse
import com.sokdak.auth.application.commands.LoginCommand
import com.sokdak.auth.application.commands.RegisterUserCommand
import com.sokdak.auth.application.results.LoginResult
import com.sokdak.auth.domain.entities.User

// Request -> Command 변환
fun RegisterUserRequest.toCommand() =
    RegisterUserCommand(
        loginId = this.loginId,
        email = this.email,
        password = this.password,
        name = this.name,
        gender = this.gender,
    )

fun LoginRequest.toCommand() =
    LoginCommand(
        loginId = this.loginId,
        password = this.password,
    )

// Domain Entity -> Response 변환
fun User.toResponse() =
    UserResponse(
        id = this.id.value,
        loginId = this.loginId.value,
        email = this.email.value,
        name = this.name,
        gender = this.gender.name,
        plan = this.plan.name,
    )

fun LoginResult.toResponse() =
    LoginResponse(
        user =
        LoginUserResponse(
            id = this.user.userId,
            name = this.user.name,
        ),
        token =
        TokenResponse(
            accessToken = this.token.accessToken,
            refreshToken = this.token.refreshToken,
            tokenType = this.token.tokenType,
            expiresInSeconds = this.token.expiresInSeconds,
        ),
    )
