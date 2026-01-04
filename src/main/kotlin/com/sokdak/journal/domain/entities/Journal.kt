package com.sokdak.journal.domain.entities

import com.sokdak.journal.domain.valueobjects.JournalId
import com.sokdak.user.domain.valueobjects.UserId
import java.time.Instant

class Journal(
    val id: JournalId,
    val userId: UserId,
    title: String,
    content: String,
    val createdAt: Instant,
    updatedAt: Instant,
) {
    var title: String = title
        private set

    var content: String = content
        private set

    var updatedAt: Instant = updatedAt
        private set

    fun update(
        title: String?,
        content: String?,
        now: Instant,
    ) {
        title?.let { this.title = it }
        content?.let { this.content = it }
        this.updatedAt = now
    }
}
