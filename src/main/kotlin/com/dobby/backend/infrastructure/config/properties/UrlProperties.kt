package com.dobby.backend.infrastructure.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app")
class UrlProperties (
    var baseUrl : String = ""
)

