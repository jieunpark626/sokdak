package com.sokdak.journal.application.usecases

import com.sokdak.journal.application.commands.UpdateJournalCommand
import com.sokdak.journal.application.exceptions.JournalNotFoundException
import com.sokdak.journal.domain.entities.Journal
import com.sokdak.journal.domain.repositories.JournalRepository
import com.sokdak.journal.domain.valueobjects.JournalId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class UpdateJournalUseCase(
    private val journalRepository: JournalRepository,
) {
    fun execute(id: String, command: UpdateJournalCommand): Journal {
        val journal = journalRepository.findById(JournalId(id))
            ?: throw JournalNotFoundException(id)

        journal.update(command.title, command.content, Instant.now())

        return journalRepository.save(journal)
    }
}