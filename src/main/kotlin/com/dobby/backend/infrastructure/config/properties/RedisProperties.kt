package com.dobby.backend.infrastructure.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spring.data.redis")
class RedisProperties {
    var host: String = ""
    var port: Int = 0
}
