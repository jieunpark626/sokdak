package com.sokdak.journal.adapter.outbound.repositories

import com.sokdak.journal.adapter.outbound.entities.JournalJpaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface JournalJpaRepository : JpaRepository<JournalJpaEntity, String> {

    fun findAllByUserId(
        userId: String,
        pageable: Pageable
    ): Page<JournalJpaEntity>

    fun findAllByUserIdAndCreatedAtBetween(
        userId: String,
        start: Instant,
        end: Instant,
        pageable: Pageable
    ): Page<JournalJpaEntity>

    fun findAllByTitleContainingIgnoreCase(
        keyword: String,
        pageable: Pageable
    ): Page<JournalJpaEntity>
}