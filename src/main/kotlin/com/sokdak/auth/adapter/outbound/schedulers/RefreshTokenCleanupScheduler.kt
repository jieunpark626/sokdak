package com.sokdak.auth.adapter.outbound.schedulers

import com.sokdak.auth.domain.repositories.RefreshTokenRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RefreshTokenCleanupScheduler(
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul") // 매일 새벽 3시 KST
    fun cleanupExpiredTokens() {
        logger.info("Starting expired refresh token cleanup")
        refreshTokenRepository.deleteExpiredTokens()
        logger.info("Completed expired refresh token cleanup")
    }
}
