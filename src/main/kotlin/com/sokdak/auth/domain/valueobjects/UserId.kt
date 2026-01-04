package com.sokdak.auth.domain.valueobjects

import de.huxhorn.sulky.ulid.ULID

@JvmInline
value class UserId(val value: String) {
    companion object {
        private val ulid = ULID()

        fun generate(): UserId {
            return UserId(ulid.nextULID())
        }
    }
}
