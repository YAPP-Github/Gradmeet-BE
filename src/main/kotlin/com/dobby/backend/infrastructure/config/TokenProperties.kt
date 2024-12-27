package com.dobby.backend.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app.token")
data class TokenProperties(
    var secretKey: String = "",
    var accessExpiration: Long = 0,
    var refreshExpiration: Long = 0,
)
