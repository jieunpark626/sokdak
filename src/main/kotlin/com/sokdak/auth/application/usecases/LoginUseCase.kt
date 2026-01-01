// application/usecases/LoginUseCase.kt
package com.sokdak.auth.application.usecases

import com.sokdak.auth.application.commands.LoginCommand
import com.sokdak.auth.application.exceptions.InvalidCredentialsException
import com.sokdak.auth.application.results.LoginResult
import com.sokdak.auth.application.results.LoginUserResult
import com.sokdak.auth.application.results.TokenResult
import com.sokdak.auth.domain.repositories.UserRepository
import com.sokdak.auth.domain.services.PasswordService
import com.sokdak.auth.domain.services.TokenService
import com.sokdak.auth.domain.valueobjects.LoginId
import com.sokdak.auth.domain.valueobjects.RawPassword
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

//TODO : 로그인 횟수 제한 ?
@Service
class LoginUseCase(
    private val userRepository: UserRepository,
    private val passwordService: PasswordService,
    private val tokenService: TokenService,
) {
    @Transactional(readOnly = true)
    fun execute(command: LoginCommand): LoginResult {
        val loginId = LoginId(command.loginId)

        val user =
            userRepository.findByLoginId(loginId)
                ?: throw InvalidCredentialsException("Invalid login credentials")

        val rawPassword = RawPassword.forLogin(command.password)
        if (!passwordService.matches(rawPassword, user.passwordHash)) {
            throw InvalidCredentialsException("Invalid login credentials")
        }

        val tokens = tokenService.generateTokens(user.id)

        return LoginResult(
            user =
            LoginUserResult(
                userId = user.id.value,
                name = user.name,
            ),
            token =
            TokenResult(
                accessToken = tokens.accessToken,
                refreshToken = tokens.refreshToken,
                expiresInSeconds = tokens.expiresInSeconds,
            ),
        )
    }
}
