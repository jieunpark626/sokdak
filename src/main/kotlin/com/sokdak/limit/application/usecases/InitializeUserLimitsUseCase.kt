package com.sokdak.limit.application.usecases

import com.sokdak.auth.domain.enums.Plan
import com.sokdak.limit.domain.entities.UserUsageLimit
import com.sokdak.limit.domain.enums.ActionType
import com.sokdak.limit.domain.repositories.UserUsageLimitRepository
import com.sokdak.limit.domain.services.PlanLimitConfig
import de.huxhorn.sulky.ulid.ULID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDate

@Service
class InitializeUserLimitsUseCase(
    private val limitRepository: UserUsageLimitRepository,
) {
    private val ulid = ULID()

    @Transactional
    fun execute(
        userId: String,
        plan: Plan,
    ): List<UserUsageLimit> {
        val now = Instant.now()
        val today = LocalDate.now()

        val limits =
            ActionType.entries.mapNotNull { action ->
                val dailyLimit = PlanLimitConfig.getLimit(plan, action)
                if (dailyLimit > 0) {
                    val existing = limitRepository.findByUserIdAndAction(userId, action)
                    existing?.let {
                        it.resetIfNeeded(today)
                        limitRepository.save(it)
                    } ?: run {
                        val newLimit =
                            UserUsageLimit(
                                id = ulid.nextULID(),
                                userId = userId,
                                action = action,
                                dailyLimit = dailyLimit,
                                dailyUsed = 0,
                                lastResetDate = today,
                                createdAt = now,
                                updatedAt = now,
                            )
                        limitRepository.save(newLimit)
                    }
                } else {
                    null
                }
            }

        return limits
    }
}
