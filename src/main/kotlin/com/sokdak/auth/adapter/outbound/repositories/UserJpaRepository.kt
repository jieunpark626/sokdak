package com.sokdak.auth.adapter.outbound.repositories

import com.sokdak.auth.adapter.outbound.entities.UserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserJpaEntity, String> {
    fun findByLoginId(loginId: String): UserJpaEntity?

    fun findByEmail(email: String): UserJpaEntity?

    fun existsByLoginId(loginId: String): Boolean

    fun existsByEmail(email: String): Boolean
}
