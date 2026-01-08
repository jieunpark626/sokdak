package com.sokdak.auth.adapter.outbound.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(
    name = "refresh_tokens",
    indexes = [
        Index(name = "idx_user_id", columnList = "user_id"),
        Index(name = "idx_token", columnList = "token"),
    ],
)
class RefreshTokenJpaEntity(
    @Id
    @Column(name = "id", nullable = false)
    var id: String,
    @Column(name = "user_id", nullable = false)
    var userId: String,
    @Column(name = "token", nullable = false, unique = true, length = 700)
    var token: String,
    @Column(name = "expires_at", nullable = false)
    var expiresAt: Instant,
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,
)
