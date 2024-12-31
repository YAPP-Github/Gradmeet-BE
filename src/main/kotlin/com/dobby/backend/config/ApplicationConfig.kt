package com.dobby.backend.config

import com.dobby.backend.infrastructure.config.properties.S3Properties
import com.dobby.backend.infrastructure.config.properties.TokenProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(TokenProperties::class, S3Properties::class)
class ApplicationConfig {
}
