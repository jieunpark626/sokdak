package com.sokdak.auth.adapter.inbound.api.mappers

import com.sokdak.auth.adapter.inbound.api.dto.requests.RegisterUserRequest
import com.sokdak.auth.adapter.inbound.api.dto.responses.UserResponse
import com.sokdak.auth.application.commands.RegisterUserCommand
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
