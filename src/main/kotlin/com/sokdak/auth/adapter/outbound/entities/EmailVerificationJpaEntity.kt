package com.sokdak.auth.adapter.outbound.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(
    name = "email_verification",
    indexes = [
        Index(name = "idx_email_verification_user_id", columnList = "user_id"),
        Index(name = "idx_email_verification_token", columnList = "token"),
        Index(name = "idx_email_verification_expires_at", columnList = "expires_at"),
    ],
)
class EmailVerificationJpaEntity(
    @Id
    @Column(name = "id", nullable = false)
    var id: String,
    @Column(name = "user_id", nullable = false)
    var userId: String,
    @Column(name = "email", nullable = false, length = 100)
    var email: String,
    @Column(name = "token", nullable = false, unique = true, length = 255)
    var token: String,
    @Column(name = "expires_at", nullable = false)
    var expiresAt: Instant,
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,
    @Column(name = "verified_at")
    var verifiedAt: Instant?,
)
