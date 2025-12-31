package com.sokdak.journal.adapter.inbound.api.dto.requests
import java.time.Instant

data class GetJournalsRequest(
    val userId: String?,
    val startDate: Instant?,
    val endDate: Instant?,
    val keyword: String?,
    val page: Int = 0,
    val size: Int = 20,
)
