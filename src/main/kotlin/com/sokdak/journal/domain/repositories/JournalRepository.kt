package com.sokdak.journal.domain.repositories

import com.sokdak.journal.domain.entities.Journal
import com.sokdak.journal.domain.valueobjects.JournalId
import com.sokdak.user.domain.valueobjects.UserId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.Instant

interface JournalRepository {

    fun save(journal: Journal): Journal
    fun findById(id: JournalId): Journal?
    fun findAll(
        userId: UserId?,
        startDate: Instant?,
        endDate: Instant?,
        keyword: String?,
        pageable: Pageable,
    ): Page<Journal>
    fun delete(id: JournalId)
}