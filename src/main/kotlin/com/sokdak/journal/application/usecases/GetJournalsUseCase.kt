package com.sokdak.journal.application.usecases

import com.sokdak.journal.application.commands.GetJournalsCommand
import com.sokdak.journal.domain.entities.Journal
import com.sokdak.journal.domain.repositories.JournalRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

import org.springframework.stereotype.Service

@Service
class GetJournalsUseCase(
    private val journalRepository: JournalRepository,
) {
    fun execute(command: GetJournalsCommand): Page<Journal> {
        val pageable = PageRequest.of(
            command.page,
            command.size,
            Sort.by("createdAt").descending()
        )

        return journalRepository.findAll(
            userId = command.userId,
            startDate = command.startDate,
            endDate = command.endDate,
            keyword = command.keyword,
            pageable = pageable,
        )
    }
}