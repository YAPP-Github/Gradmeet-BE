package com.dobby.backend.presentation.api.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "cors")
class AllowedOriginsProperties {
    var allowedOrigins: List<String> = listOf()
}
