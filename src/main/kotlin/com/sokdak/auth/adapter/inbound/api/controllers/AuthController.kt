package com.sokdak.auth.adapter.inbound.api.controllers

import com.sokdak.auth.adapter.inbound.api.dto.requests.RegisterUserRequest
import com.sokdak.auth.adapter.inbound.api.dto.responses.UserResponse
import com.sokdak.auth.adapter.inbound.api.mappers.toCommand
import com.sokdak.auth.adapter.inbound.api.mappers.toResponse
import com.sokdak.auth.application.usecases.RegisterUserUseCase
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
}
