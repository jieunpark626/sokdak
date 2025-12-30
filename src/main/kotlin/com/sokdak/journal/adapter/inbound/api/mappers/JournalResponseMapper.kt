package com.sokdak.journal.adapter.inbound.api.mappers

import com.sokdak.journal.adapter.inbound.api.dto.responses.JournalResponse
import com.sokdak.journal.adapter.inbound.api.dto.responses.JournalSummaryResponse
import com.sokdak.journal.domain.entities.Journal


fun Journal.toResponse(): JournalResponse =
    JournalResponse(
        id = this.id.value,
        userId = this.userId.value,
        title = this.title,
        content = this.content,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),
    )

fun Journal.toSummaryResponse(): JournalSummaryResponse =
    JournalSummaryResponse(
        id = this.id.value,
        title = this.title,
        createdAt = this.createdAt.toString(),
    )