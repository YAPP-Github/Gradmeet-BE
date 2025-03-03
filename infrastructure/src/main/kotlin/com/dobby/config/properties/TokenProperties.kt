package com.dobby.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app.token")
data class TokenProperties(
    var secretKey: String = "",
    var expiration: Expiration = Expiration()
) {
    data class Expiration(
        var access: Long = 0,
        var refresh: Long = 0
    )
}
