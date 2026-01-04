package com.sokdak.journal.adapter.outbound.mappers

import com.sokdak.journal.adapter.outbound.entities.JournalJpaEntity
import com.sokdak.journal.domain.entities.Journal
import com.sokdak.journal.domain.valueobjects.JournalId
import com.sokdak.user.domain.valueobjects.UserId
import org.springframework.stereotype.Component

@Component
class JournalMapper {
    fun toJpaEntity(journal: Journal): JournalJpaEntity =
        JournalJpaEntity(
            id = journal.id.value,
            userId = journal.userId.value,
            title = journal.title,
            content = journal.content,
            createdAt = journal.createdAt,
            updatedAt = journal.updatedAt,
        )

    fun toDomain(entity: JournalJpaEntity): Journal =
        Journal(
            id = JournalId(entity.id),
            userId = UserId(entity.userId),
            title = entity.title,
            content = entity.content,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
}
