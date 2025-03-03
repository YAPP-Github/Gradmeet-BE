package com.dobby.config

import com.dobby.token.JwtTokenManager
import com.dobby.token.JwtTokenProvider
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
