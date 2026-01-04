package com.sokdak.journal.application.usecases

import com.sokdak.journal.domain.repositories.JournalRepository
import com.sokdak.journal.domain.valueobjects.JournalId
import org.springframework.stereotype.Service

@Service
class DeleteJournalUseCase(
    private val journalRepository: JournalRepository,
) {
    fun execute(id: String) {
        journalRepository.delete(JournalId(id))
    }
}
