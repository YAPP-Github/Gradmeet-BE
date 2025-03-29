package com.dobby.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
data class GoogleAuthProperties(
    val grantType: String = "authorization_code",
    var clientId: String = "",
    var clientSecret: String = "",
    var redirectUri: String = ""
)
