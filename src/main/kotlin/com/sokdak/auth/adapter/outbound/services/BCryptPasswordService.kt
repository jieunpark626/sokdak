package com.sokdak.auth.adapter.outbound.services

import com.sokdak.auth.domain.services.PasswordService
import com.sokdak.auth.domain.valueobjects.HashedPassword
import com.sokdak.auth.domain.valueobjects.RawPassword
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class BCryptPasswordService(
    private val encoder: PasswordEncoder,
) : PasswordService {
    override fun hash(rawPassword: RawPassword): HashedPassword {
        return HashedPassword(encoder.encode(rawPassword.value))
    }

    override fun matches(
        rawPassword: RawPassword,
        hashedPassword: HashedPassword,
    ): Boolean {
        return encoder.matches(rawPassword.value, hashedPassword.value)
    }
}
