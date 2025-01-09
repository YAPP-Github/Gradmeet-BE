package com.dobby.backend.config

import com.dobby.backend.application.service.AuthService
import com.dobby.backend.application.usecase.FetchGoogleUserInfoUseCase
import com.dobby.backend.application.usecase.FetchNaverUserInfoUseCase
import com.dobby.backend.application.usecase.GenerateTestTokenUseCase
import com.dobby.backend.application.usecase.GenerateTokenWithRefreshTokenUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
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
