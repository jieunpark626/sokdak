package com.sokdak.journal.adapter.inbound.api.dto.requests

data class UpdateJournalRequest(
    val title: String? = null,
    val content: String? = null,
)
