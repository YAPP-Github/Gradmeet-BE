package com.dobby.backend.config

import com.dobby.backend.infrastructure.config.TokenProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(TokenProperties::class)
class ApplicationConfig {
}
