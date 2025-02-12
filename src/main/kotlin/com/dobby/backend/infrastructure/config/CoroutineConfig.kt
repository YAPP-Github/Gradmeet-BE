package com.dobby.backend.infrastructure.config

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoroutineConfig {
    @Bean
    fun coroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }
}
