package com.sokdak.journal.adapter.inbound.api.dto.requests

data class CreateJournalRequest(
    val userId: String,
    val title: String,
    val content: String,
)
