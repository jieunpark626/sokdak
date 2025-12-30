package com.sokdak.journal.adapter.inbound.api.dto.responses

data class JournalResponse(
    val id: String,
    val userId: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
)