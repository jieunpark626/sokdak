package com.sokdak.journal.application.usecases

import com.sokdak.journal.application.commands.CreateJournalCommand
import com.sokdak.journal.domain.entities.Journal
import com.sokdak.journal.domain.repositories.JournalRepository
import com.sokdak.journal.domain.valueobjects.JournalId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class CreateJournalUseCase(
    private val journalRepository: JournalRepository,
) {
    fun execute(command: CreateJournalCommand): Journal {
        val now = Instant.now()

        val journal = Journal(
            id = JournalId.generate(),
            userId = command.userId,
            title = command.title,
            content = command.content,
            createdAt = now,
            updatedAt = now,
        )

        return journalRepository.save(journal)
    }
}