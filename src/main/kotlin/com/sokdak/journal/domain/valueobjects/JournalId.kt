package com.sokdak.journal.domain.valueobjects

import de.huxhorn.sulky.ulid.ULID

@JvmInline
value class JournalId(val value: String) {
    companion object {
        private val ulid = ULID()

        fun generate(): JournalId {
            return JournalId(ulid.nextULID())
        }
    }
}
