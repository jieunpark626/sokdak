package com.sokdak.journal.application.commands

import com.sokdak.user.domain.valueobjects.UserId

data class CreateJournalCommand(
    val userId: UserId,
    val title: String,
    val content: String,
)
