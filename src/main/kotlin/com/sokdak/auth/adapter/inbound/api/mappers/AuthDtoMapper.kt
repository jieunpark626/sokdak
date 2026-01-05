package com.sokdak.auth.adapter.inbound.api.mappers

import com.sokdak.auth.adapter.inbound.api.dto.requests.LoginRequest
import com.sokdak.auth.adapter.inbound.api.dto.requests.LogoutRequest
import com.sokdak.auth.adapter.inbound.api.dto.requests.RefreshTokenRequest
import com.sokdak.auth.adapter.inbound.api.dto.requests.RegisterUserRequest
import com.sokdak.auth.adapter.inbound.api.dto.requests.VerifyTokenRequest
import com.sokdak.auth.adapter.inbound.api.dto.responses.LoginResponse
import com.sokdak.auth.adapter.inbound.api.dto.responses.LoginUserResponse
import com.sokdak.auth.adapter.inbound.api.dto.responses.TokenResponse
import com.sokdak.auth.adapter.inbound.api.dto.responses.UserResponse
import com.sokdak.auth.adapter.inbound.api.dto.responses.VerifyTokenResponse
import com.sokdak.auth.application.commands.LoginCommand
import com.sokdak.auth.application.commands.LogoutCommand
import com.sokdak.auth.application.commands.RefreshTokenCommand
import com.sokdak.auth.application.commands.RegisterUserCommand
import com.sokdak.auth.application.commands.VerifyTokenCommand
import com.sokdak.auth.application.dto.LoginResult
import com.sokdak.auth.application.dto.VerifyTokenResult
import com.sokdak.auth.domain.entities.User
import com.sokdak.auth.domain.valueobjects.AuthTokens

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

fun RefreshTokenRequest.toCommand() =
    RefreshTokenCommand(
        refreshToken = this.refreshToken,
    )

fun VerifyTokenRequest.toCommand() =
    VerifyTokenCommand(
        token = this.token,
    )

fun LogoutRequest.toCommand() =
    LogoutCommand(
        refreshToken = this.refreshToken,
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
        tokens =
            TokenResponse(
                accessToken = this.tokens.accessToken,
                refreshToken = this.tokens.refreshToken,
                expiresInSeconds = this.tokens.expiresInSeconds,
            ),
    )

fun AuthTokens.toResponse() =
    TokenResponse(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        tokenType = "Bearer",
        expiresInSeconds = this.expiresInSeconds,
    )

fun VerifyTokenResult.toResponse() =
    VerifyTokenResponse(
        valid = this.valid,
        userId = this.userId,
    )
