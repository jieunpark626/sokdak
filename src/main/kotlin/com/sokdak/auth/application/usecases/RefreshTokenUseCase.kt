package com.sokdak.auth.application.usecases

import com.sokdak.auth.application.commands.RefreshTokenCommand
import com.sokdak.auth.application.exceptions.InvalidTokenException
import com.sokdak.auth.domain.entities.RefreshToken
import com.sokdak.auth.domain.repositories.RefreshTokenRepository
import com.sokdak.auth.domain.services.TokenService
import com.sokdak.auth.domain.valueobjects.AuthTokens
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

// TODO: 동시성 문제
@Service
class RefreshTokenUseCase(
    private val tokenService: TokenService,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    @Transactional
    fun execute(command: RefreshTokenCommand): AuthTokens {
        // 1. JWT 자체 유효성 검증 (서명, 만료 시간)
        val userId = tokenService.validateRefreshToken(command.refreshToken)

        // 2. DB에 저장된 Refresh Token과 비교
        val storedToken =
            refreshTokenRepository.findByToken(command.refreshToken)
                ?: throw InvalidTokenException("Refresh token not found in database")

        // 3. 토큰이 만료되었는지 확인
        if (storedToken.isExpired()) {
            refreshTokenRepository.deleteByToken(command.refreshToken)
            throw InvalidTokenException("Refresh token has expired")
        }

        // 4. userId가 일치하는지 확인
        if (storedToken.userId != userId) {
            throw InvalidTokenException("User ID mismatch")
        }
        // 5. 새로운 Access Token과 Refresh Token 발급
        val newTokens = tokenService.generateTokens(userId)

        // 7. 기존 Refresh Token 삭제 (Refresh Token Rotation)
        refreshTokenRepository.deleteByToken(command.refreshToken)

        // 8. 새로운 Refresh Token을 DB에 저장
        val refreshTokenExpiry = Instant.now().plusSeconds(newTokens.refreshTokenExpiresInSeconds)
        val newRefreshToken =
            RefreshToken.create(
                userId = userId,
                token = newTokens.refreshToken,
                expiresAt = refreshTokenExpiry,
            )
        refreshTokenRepository.save(newRefreshToken)

        return newTokens
    }
}
