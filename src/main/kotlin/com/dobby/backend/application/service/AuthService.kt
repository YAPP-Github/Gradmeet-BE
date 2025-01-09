package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.*
import com.dobby.backend.presentation.api.dto.request.auth.google.GoogleOauthLoginRequest
import com.dobby.backend.presentation.api.dto.response.auth.OauthLoginResponse
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val fetchGoogleUserInfoUseCase: FetchGoogleUserInfoUseCase,
    private val fetchNaverUserInfoUseCase: FetchNaverUserInfoUseCase,
    private val generateTokenWithRefreshTokenUseCase: GenerateTokenWithRefreshTokenUseCase,
    private val generateTestTokenUseCase: GenerateTestTokenUseCase,
) {
    fun getGoogleUserInfo(oauthLoginRequest: GoogleOauthLoginRequest): OauthLoginResponse {
        return fetchGoogleUserInfoUseCase.execute(oauthLoginRequest)
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
            oauthEmail = result.oauthEmail,
            oauthName = result.oauthName,
            role = result.role,
            provider = result.provider,
        )
    }

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
