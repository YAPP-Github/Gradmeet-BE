package com.dobby.backend.infrastructure.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "swagger")
class UrlProperties (
    var serverUrl : String = ""
)

