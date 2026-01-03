package com.sokdak.auth.application.usecases

import com.sokdak.auth.application.commands.RefreshTokenCommand
import com.sokdak.auth.domain.services.TokenService
import com.sokdak.auth.domain.valueobjects.AuthTokens
import org.springframework.stereotype.Service

@Service
class RefreshTokenUseCase(
    private val tokenService: TokenService,
) {
    fun execute(command: RefreshTokenCommand): AuthTokens {
        val userId = tokenService.validateRefreshToken(command.refreshToken)

        return tokenService.generateTokens(userId)
    }
}
