package com.dobby.service

import com.dobby.usecase.auth.FetchGoogleUserInfoUseCase
import com.dobby.usecase.auth.FetchNaverUserInfoUseCase
import com.dobby.usecase.auth.GenerateTestTokenUseCase
import com.dobby.usecase.auth.GenerateTokenWithRefreshTokenUseCase
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val fetchGoogleUserInfoUseCase: FetchGoogleUserInfoUseCase,
    private val fetchNaverUserInfoUseCase: FetchNaverUserInfoUseCase,
    private val generateTokenWithRefreshTokenUseCase: GenerateTokenWithRefreshTokenUseCase,
    private val generateTestTokenUseCase: GenerateTestTokenUseCase
) {
    fun getGoogleUserInfo(input: FetchGoogleUserInfoUseCase.Input): FetchGoogleUserInfoUseCase.Output {
        return fetchGoogleUserInfoUseCase.execute(input)
    }

    fun getNaverUserInfo(input: FetchNaverUserInfoUseCase.Input): FetchNaverUserInfoUseCase.Output {
        return fetchNaverUserInfoUseCase.execute(input)
    }

    @Transactional
    fun forceToken(input: GenerateTestTokenUseCase.Input): GenerateTestTokenUseCase.Output {
        return generateTestTokenUseCase.execute(input)
    }

    @Transactional
    fun signInWithRefreshToken(input: GenerateTokenWithRefreshTokenUseCase.Input): GenerateTokenWithRefreshTokenUseCase.Output {
        return generateTokenWithRefreshTokenUseCase.execute(input)
    }
}
