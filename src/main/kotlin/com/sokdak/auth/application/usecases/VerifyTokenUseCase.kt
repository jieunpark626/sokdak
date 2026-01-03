package com.sokdak.auth.application.usecases

import com.sokdak.auth.application.commands.VerifyTokenCommand
import com.sokdak.auth.application.dto.VerifyTokenResult
import com.sokdak.auth.domain.services.TokenService
import org.springframework.stereotype.Service

@Service
class VerifyTokenUseCase(
    private val tokenService: TokenService,
) {
    fun execute(command: VerifyTokenCommand): VerifyTokenResult {
        return try {
            val userId = tokenService.validateToken(command.token)
            VerifyTokenResult(
                valid = true,
                userId = userId.value,
            )
        } catch (e: Exception) {
            VerifyTokenResult(
                valid = false,
                userId = null,
            )
        }
    }
}
