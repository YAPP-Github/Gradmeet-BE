package com.dobby.backend.config

import com.dobby.backend.application.service.AuthService
import com.dobby.backend.application.usecase.FetchGoogleUserInfoUseCase
import com.dobby.backend.application.usecase.FetchNaverUserInfoUseCase
import com.dobby.backend.application.usecase.GenerateTestTokenUseCase
import com.dobby.backend.application.usecase.GenerateTokenWithRefreshTokenUseCase
import com.dobby.backend.infrastructure.config.properties.GoogleAuthProperties
import com.dobby.backend.infrastructure.config.properties.NaverAuthProperties
import com.dobby.backend.infrastructure.config.properties.S3Properties
import com.dobby.backend.infrastructure.config.properties.TokenProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(TokenProperties::class, S3Properties::class, GoogleAuthProperties::class, NaverAuthProperties::class)
class ApplicationConfig {
    @Bean
    fun authService(
        fetchGoogleUserInfoUseCase: FetchGoogleUserInfoUseCase,
        fetchNaverUserInfoUseCase: FetchNaverUserInfoUseCase,
        generateTokenWithRefreshTokenUseCase: GenerateTokenWithRefreshTokenUseCase,
        generateTestTokenUseCase: GenerateTestTokenUseCase
    ): AuthService {
        return AuthService(
            fetchGoogleUserInfoUseCase,
            fetchNaverUserInfoUseCase,
            generateTokenWithRefreshTokenUseCase,
            generateTestTokenUseCase
        )
    }
}
