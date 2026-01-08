package com.sokdak.auth.domain.services

import com.sokdak.auth.domain.valueobjects.Email

interface EmailService {
    fun sendVerificationEmail(
        recipientEmail: Email,
        recipientName: String,
        verificationToken: String,
    )

    fun sendWelcomeEmail(
        recipientEmail: Email,
        recipientName: String,
    )
}
