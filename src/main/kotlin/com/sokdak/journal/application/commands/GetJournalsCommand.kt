package com.sokdak.journal.application.commands

import com.sokdak.user.domain.valueobjects.UserId
import java.time.Instant

data class GetJournalsCommand(
    val userId: UserId?,
    val startDate: Instant?,
    val endDate: Instant?,
    val keyword: String?,
    val page: Int,
    val size: Int,
)
