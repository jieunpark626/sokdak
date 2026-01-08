package com.sokdak.auth.adapter.outbound.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "email.verification")
data class EmailProperties(
    var tokenValidityInSeconds: Long = 3600,
    var fromAddress: String = "",
    var fromName: String = "Sokdak",
    var baseUrl: String = "",
)
