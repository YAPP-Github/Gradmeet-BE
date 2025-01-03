package com.dobby.backend.infrastructure.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spring.security.oauth.client.registration.google")
data class GoogleAuthProperties (
    var clientId: String = "",
    var clientSecret: String= "",
    var redirectUri : String = ""
)
