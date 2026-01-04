package com.sokdak.auth.domain.services

import com.sokdak.auth.domain.valueobjects.HashedPassword
import com.sokdak.auth.domain.valueobjects.RawPassword

interface PasswordService {
    fun hash(rawPassword: RawPassword): HashedPassword

    fun matches(
        rawPassword: RawPassword,
        hashedPassword: HashedPassword,
    ): Boolean
}
