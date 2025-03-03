package com.dobby.backend.infrastructure.config

import JwtTokenManager
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.token.SecurityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {
    @Bean
    fun securityManager(): SecurityManager {
        return JwtTokenManager(jwtTokenProvider)
    }
}
