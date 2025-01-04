package com.dobby.backend.infrastructure.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spring.security.oauth.client.registration.naver")
data class NaverAuthProperties (
    var clientId: String = "",
    var clientSecret: String= "",
    var redirectUri : String = ""
)
