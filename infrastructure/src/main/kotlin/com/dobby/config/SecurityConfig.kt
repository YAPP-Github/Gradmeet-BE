package com.dobby.config

import com.dobby.security.SecurityManager
import com.dobby.security.token.JwtTokenManager
import com.dobby.security.token.JwtTokenProvider
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
