package com.sokdak.journal.application.exceptions

class JournalNotFoundException(
    journalId: String,
) : RuntimeException("Journal not found. id=$journalId")
