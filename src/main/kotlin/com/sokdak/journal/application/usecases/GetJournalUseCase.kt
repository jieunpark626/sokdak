package com.sokdak.journal.application.usecases

import com.sokdak.journal.application.exceptions.JournalNotFoundException
import com.sokdak.journal.domain.entities.Journal
import com.sokdak.journal.domain.repositories.JournalRepository
import com.sokdak.journal.domain.valueobjects.JournalId
import org.springframework.stereotype.Service

@Service
class GetJournalUseCase(
    private val journalRepository: JournalRepository,
) {
    fun execute(id: String): Journal =
        journalRepository.findById(JournalId(id))
            ?: throw JournalNotFoundException(id)
}
