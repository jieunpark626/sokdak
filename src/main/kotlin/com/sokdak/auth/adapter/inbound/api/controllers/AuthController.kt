package com.sokdak.auth.adapter.inbound.api.controllers

import com.sokdak.auth.adapter.inbound.api.dto.requests.LoginRequest
import com.sokdak.auth.adapter.inbound.api.dto.requests.LogoutRequest
import com.sokdak.auth.adapter.inbound.api.dto.requests.RefreshTokenRequest
import com.sokdak.auth.adapter.inbound.api.dto.requests.RegisterUserRequest
import com.sokdak.auth.adapter.inbound.api.dto.requests.VerifyTokenRequest
import com.sokdak.auth.adapter.inbound.api.dto.responses.LoginResponse
import com.sokdak.auth.adapter.inbound.api.dto.responses.TokenResponse
import com.sokdak.auth.adapter.inbound.api.dto.responses.UserResponse
import com.sokdak.auth.adapter.inbound.api.dto.responses.VerifyTokenResponse
import com.sokdak.auth.adapter.inbound.api.mappers.toCommand
import com.sokdak.auth.adapter.inbound.api.mappers.toResponse
import com.sokdak.auth.application.usecases.LoginUseCase
import com.sokdak.auth.application.usecases.LogoutUseCase
import com.sokdak.auth.application.usecases.RefreshTokenUseCase
import com.sokdak.auth.application.usecases.RegisterUserUseCase
import com.sokdak.auth.application.usecases.VerifyTokenUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginUseCase: LoginUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val verifyTokenUseCase: VerifyTokenUseCase,
    private val logoutUseCase: LogoutUseCase,
) {
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: RegisterUserRequest,
    ): ResponseEntity<UserResponse> {
        val command = request.toCommand()
        val user = registerUserUseCase.execute(command)
        val response = user.toResponse()

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
    ): ResponseEntity<LoginResponse> {
        val command = request.toCommand()
        val result = loginUseCase.execute(command)
        val response = result.toResponse()

        return ResponseEntity.ok(response)
    }

    @PostMapping("/refresh")
    fun refresh(
        @Valid @RequestBody request: RefreshTokenRequest,
    ): ResponseEntity<TokenResponse> {
        val command = request.toCommand()
        val tokens = refreshTokenUseCase.execute(command)
        val response = tokens.toResponse()

        return ResponseEntity.ok(response)
    }

    @PostMapping("/verify")
    fun verify(
        @Valid @RequestBody request: VerifyTokenRequest,
    ): ResponseEntity<VerifyTokenResponse> {
        val command = request.toCommand()
        val result = verifyTokenUseCase.execute(command)
        val response = result.toResponse()

        return ResponseEntity.ok(response)
    }

    @PostMapping("/logout")
    fun logout(
        @Valid @RequestBody request: LogoutRequest,
    ): ResponseEntity<Void> {
        val command = request.toCommand()
        logoutUseCase.execute(command)

        return ResponseEntity.noContent().build()
    }
}
