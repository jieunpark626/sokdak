package com.sokdak.auth.adapter.outbound.services

import com.sokdak.auth.adapter.outbound.config.EmailProperties
import com.sokdak.auth.domain.services.EmailService
import com.sokdak.auth.domain.valueobjects.Email
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class EmailServiceImpl(
    private val mailSender: JavaMailSender,
    private val templateEngine: TemplateEngine,
    private val emailProperties: EmailProperties,
) : EmailService {
    override fun sendVerificationEmail(
        recipientEmail: Email,
        recipientName: String,
        verificationToken: String,
    ) {
        val verificationUrl = "${emailProperties.baseUrl}/auth/verify-email?token=$verificationToken"

        val context =
            Context().apply {
                setVariable("name", recipientName)
                setVariable("verificationUrl", verificationUrl)
                setVariable("expiryHours", emailProperties.tokenValidityInSeconds / 3600)
            }

        val htmlContent = templateEngine.process("email-verification", context)

        sendEmail(
            to = recipientEmail.value,
            subject = "Verify Your Email - Sokdak",
            htmlContent = htmlContent,
        )
    }

    override fun sendWelcomeEmail(
        recipientEmail: Email,
        recipientName: String,
    ) {
        val context =
            Context().apply {
                setVariable("name", recipientName)
            }

        val htmlContent = templateEngine.process("welcome-email", context)

        sendEmail(
            to = recipientEmail.value,
            subject = "Welcome to Sokdak!",
            htmlContent = htmlContent,
        )
    }

    private fun sendEmail(
        to: String,
        subject: String,
        htmlContent: String,
    ) {
        val message: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setFrom(emailProperties.fromAddress, emailProperties.fromName)
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(htmlContent, true)

        mailSender.send(message)
    }
}
