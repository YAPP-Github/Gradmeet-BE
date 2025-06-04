package com.dobby.auth

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "oauth.google")
data class GoogleRedirectUriProperties(
    var redirectUris: List<String> = emptyList()
)
