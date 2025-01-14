package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.auth.FetchGoogleUserInfoUseCase
import com.dobby.backend.application.usecase.auth.FetchNaverUserInfoUseCase
import com.dobby.backend.application.usecase.auth.GenerateTestTokenUseCase
import com.dobby.backend.application.usecase.auth.GenerateTokenWithRefreshTokenUseCase
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val fetchGoogleUserInfoUseCase: FetchGoogleUserInfoUseCase,
    private val fetchNaverUserInfoUseCase: FetchNaverUserInfoUseCase,
    private val generateTokenWithRefreshTokenUseCase: GenerateTokenWithRefreshTokenUseCase,
    private val generateTestTokenUseCase: GenerateTestTokenUseCase,
) {
    fun getGoogleUserInfo(authorizationCode: String): FetchGoogleUserInfoUseCase.Output {
        val result = fetchGoogleUserInfoUseCase.execute(
            FetchGoogleUserInfoUseCase.Input(
                authorizationCode = authorizationCode
            )
        )
        return FetchGoogleUserInfoUseCase.Output(
            isRegistered = result.isRegistered,
            accessToken = result.accessToken,
            refreshToken = result.refreshToken,
            memberId = result.memberId,
            name = result.name,
            oauthEmail = result.oauthEmail,
            role = result.role,
            provider = result.provider,
        )
    }

    fun getNaverUserInfo(authorizationCode: String, state: String): FetchNaverUserInfoUseCase.Output {
        val result = fetchNaverUserInfoUseCase.execute(
            FetchNaverUserInfoUseCase.Input(
                authorizationCode = authorizationCode,
                state = state
            )
        )
        return FetchNaverUserInfoUseCase.Output(
            isRegistered = result.isRegistered,
            accessToken = result.accessToken,
            refreshToken = result.refreshToken,
            memberId = result.memberId,
            name = result.name,
            oauthEmail = result.oauthEmail,
            role = result.role,
            provider = result.provider,
        )
    }

    @Transactional
    fun forceToken(memberId: Long): GenerateTestTokenUseCase.Output {
        val result = generateTestTokenUseCase.execute(
            GenerateTestTokenUseCase.Input(
                memberId = memberId
            )
        )
        return GenerateTestTokenUseCase.Output(
            accessToken = result.accessToken,
            refreshToken = result.refreshToken,
            member = result.member
        )
    }

    @Transactional
    fun signInWithRefreshToken(refreshToken: String): GenerateTokenWithRefreshTokenUseCase.Output {
        val result = generateTokenWithRefreshTokenUseCase.execute(
            GenerateTokenWithRefreshTokenUseCase.Input(
                refreshToken = refreshToken,
            )
        )
        return GenerateTokenWithRefreshTokenUseCase.Output(
            accessToken = result.accessToken,
            refreshToken = result.refreshToken,
            member = result.member
        )
    }
}
