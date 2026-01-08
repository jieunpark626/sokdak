package com.sokdak.auth.application.usecases

import com.sokdak.auth.application.commands.LogoutCommand
import com.sokdak.auth.domain.repositories.RefreshTokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LogoutUseCase(
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    @Transactional
    fun execute(command: LogoutCommand) {
        // TODO : access token 도 만료? , DB에 없을 때 에러?
        refreshTokenRepository.deleteByToken(command.refreshToken)
    }
}
