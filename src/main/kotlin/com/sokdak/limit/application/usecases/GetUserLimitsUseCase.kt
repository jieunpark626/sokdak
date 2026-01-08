package com.sokdak.limit.application.usecases

import com.sokdak.limit.domain.entities.UserUsageLimit
import com.sokdak.limit.domain.repositories.UserUsageLimitRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class GetUserLimitsUseCase(
    private val limitRepository: UserUsageLimitRepository,
) {
    fun execute(userId: String): List<UserUsageLimit> {
        val limits = limitRepository.findAllByUserId(userId)
        val today = LocalDate.now()

        return limits.map { limit ->
            limit.resetIfNeeded(today)
            limit
        }
    }
}
