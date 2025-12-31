package com.sokdak.auth.adapter.outbound.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "users")
class UserJpaEntity(
    @Id
    @Column(name = "id", nullable = false)
    var id: String,
    @Column(name = "login_id", nullable = false, unique = true, length = 50)
    var loginId: String,
    @Column(nullable = false, unique = true, length = 100)
    var email: String,
    @Column(name = "password_hash", nullable = false, length = 255)
    var passwordHash: String,
    @Column(nullable = false, length = 100)
    var name: String,
    @Column(nullable = false, length = 10)
    var gender: String,
    @Column(nullable = false, length = 20)
    var plan: String,
    @Column(name = "created_at")
    val createdAt: Instant,
    @Column(name = "updated_at")
    var updatedAt: Instant,
)
