package com.sokdak.journal.application.commands

import com.sokdak.common.domain.valueobjects.UserId

data class CreateJournalCommand(
    val userId: UserId,
    val title: String,
    val content: String,
)
