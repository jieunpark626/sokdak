package com.sokdak.journal.adapter.outbound.repositories

import com.sokdak.common.domain.valueobjects.UserId
import com.sokdak.journal.adapter.outbound.mappers.JournalMapper
import com.sokdak.journal.domain.entities.Journal
import com.sokdak.journal.domain.repositories.JournalRepository
import com.sokdak.journal.domain.valueobjects.JournalId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class JournalRepositoryImpl(
    private val jpaRepository: JournalJpaRepository,
    private val mapper: JournalMapper,
) : JournalRepository {
    override fun save(journal: Journal): Journal {
        val entity = mapper.toJpaEntity(journal)
        val saved = jpaRepository.save(entity)
        return mapper.toDomain(saved)
    }

    override fun findById(id: JournalId): Journal? {
        return jpaRepository.findById(id.value)
            .map { mapper.toDomain(it) }
            .orElse(null)
    }

    override fun findAll(
        userId: UserId?,
        startDate: Instant?,
        endDate: Instant?,
        keyword: String?,
        pageable: Pageable,
    ): Page<Journal> {
        val page =
            when {
                userId != null && startDate != null && endDate != null ->
                    jpaRepository.findAllByUserIdAndCreatedAtBetween(
                        userId = userId.value,
                        start = startDate,
                        end = endDate,
                        pageable = pageable,
                    )

                userId != null ->
                    jpaRepository.findAllByUserId(
                        userId.value,
                        pageable,
                    )

                keyword != null ->
                    jpaRepository.findAllByTitleContainingIgnoreCase(
                        keyword,
                        pageable,
                    )

                else ->
                    jpaRepository.findAll(pageable)
            }

        return page.map(mapper::toDomain)
    }

    override fun delete(id: JournalId) {
        if (jpaRepository.existsById(id.value)) {
            jpaRepository.deleteById(id.value)
        }
    }
}
