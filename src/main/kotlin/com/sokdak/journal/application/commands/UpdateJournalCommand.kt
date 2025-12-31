package com.sokdak.journal.application.commands

data class UpdateJournalCommand(
    val title: String? = null,
    val content: String? = null,
)