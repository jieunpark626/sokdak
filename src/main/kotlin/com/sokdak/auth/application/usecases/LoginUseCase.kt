// application/usecases/LoginUseCase.kt
package com.sokdak.auth.application.usecases

import com.sokdak.auth.application.commands.LoginCommand
import com.sokdak.auth.application.dto.AuthTokenDto
import com.sokdak.auth.application.dto.LoginResult
import com.sokdak.auth.application.dto.UserDto
import com.sokdak.auth.application.exceptions.InvalidCredentialsException
import com.sokdak.auth.domain.entities.RefreshToken
import com.sokdak.auth.domain.repositories.RefreshTokenRepository
import com.sokdak.auth.domain.repositories.UserRepository
import com.sokdak.auth.domain.services.PasswordService
import com.sokdak.auth.domain.services.TokenService
import com.sokdak.auth.domain.valueobjects.LoginId
import com.sokdak.auth.domain.valueobjects.RawPassword
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

// TODO : 로그인 횟수 제한 ?
@Service
class LoginUseCase(
    private val userRepository: UserRepository,
    private val passwordService: PasswordService,
    private val tokenService: TokenService,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    @Transactional
    fun execute(command: LoginCommand): LoginResult {
        val loginId = LoginId(command.loginId)

        val user =
            userRepository.findByLoginId(loginId)
                ?: throw InvalidCredentialsException("Invalid login credentials")

        val rawPassword = RawPassword.forLogin(command.password)
        if (!passwordService.matches(rawPassword, user.passwordHash)) {
            throw InvalidCredentialsException("Invalid login credentials")
        }

        // 토큰 발급
        val tokens = tokenService.generateTokens(user.id)

        // Refresh Token을 DB에 저장
        val refreshTokenExpiry = Instant.now().plusSeconds(tokens.refreshTokenExpiresInSeconds)
        val refreshToken =
            RefreshToken.create(
                userId = user.id,
                token = tokens.refreshToken,
                expiresAt = refreshTokenExpiry,
            )
        refreshTokenRepository.save(refreshToken)

        return LoginResult(
            user =
                UserDto(
                    userId = user.id.value,
                    name = user.name,
                ),
            tokens =
                AuthTokenDto(
                    accessToken = tokens.accessToken,
                    refreshToken = tokens.refreshToken,
                    expiresInSeconds = tokens.expiresInSeconds,
                ),
        )
    }
}
