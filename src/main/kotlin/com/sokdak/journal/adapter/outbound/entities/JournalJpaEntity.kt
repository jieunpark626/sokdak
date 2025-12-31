package com.sokdak.journal.adapter.outbound.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "journals")
class JournalJpaEntity(

    @Id
    val id: String,

    @Column(nullable = false)
    val userId: String,

    @Column(nullable = false, length=50)
    var title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    val createdAt: Instant,

    var updatedAt: Instant,
)