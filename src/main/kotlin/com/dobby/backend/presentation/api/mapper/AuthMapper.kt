package com.dobby.backend.presentation.api.mapper

import com.dobby.backend.application.usecase.auth.FetchGoogleUserInfoUseCase
import com.dobby.backend.application.usecase.auth.FetchNaverUserInfoUseCase
import com.dobby.backend.application.usecase.auth.GenerateTestTokenUseCase
import com.dobby.backend.application.usecase.auth.GenerateTokenWithRefreshTokenUseCase
import com.dobby.backend.presentation.api.dto.response.auth.OauthLoginResponse
import com.dobby.backend.presentation.api.dto.response.auth.TestMemberSignInResponse
import com.dobby.backend.presentation.api.dto.response.member.MemberResponse
import com.dobby.backend.presentation.api.dto.request.auth.GoogleOauthLoginRequest
import com.dobby.backend.presentation.api.dto.request.auth.MemberRefreshTokenRequest
import com.dobby.backend.presentation.api.dto.request.auth.NaverOauthLoginRequest

object AuthMapper {
    fun toGoogleOauthLoginInput(request: GoogleOauthLoginRequest): FetchGoogleUserInfoUseCase.Input {
        return FetchGoogleUserInfoUseCase.Input(
            authorizationCode = request.authorizationCode
        )
    }

    fun toGoogleOauthLoginResponse(output: FetchGoogleUserInfoUseCase.Output): OauthLoginResponse {
        return OauthLoginResponse(
            isRegistered = output.isRegistered,
            accessToken = output.accessToken,
            refreshToken = output.refreshToken,
            memberInfo = MemberResponse(
                memberId = output.memberId,
                name = output.name,
                oauthEmail = output.oauthEmail,
                role = output.role,
                provider = output.provider
            )
        )
    }

    fun toNaverOauthLoginInput(request: NaverOauthLoginRequest): FetchNaverUserInfoUseCase.Input {
        return FetchNaverUserInfoUseCase.Input(
            authorizationCode = request.authorizationCode,
            state = request.state
        )
    }

    fun toNaverOauthLoginResponse(output: FetchNaverUserInfoUseCase.Output): OauthLoginResponse {
        return OauthLoginResponse(
            isRegistered = output.isRegistered,
            accessToken = output.accessToken,
            refreshToken = output.refreshToken,
            memberInfo = MemberResponse(
                memberId = output.memberId,
                name = output.name,
                oauthEmail = output.oauthEmail,
                role = output.role,
                provider = output.provider
            )
        )
    }

    fun toForceTokenInput(input: Long): GenerateTestTokenUseCase.Input {
        return GenerateTestTokenUseCase.Input(
            memberId = input
        )
    }

    fun toTestMemberSignInResponse(output: GenerateTestTokenUseCase.Output): TestMemberSignInResponse {
        return TestMemberSignInResponse(
            accessToken = output.accessToken,
            refreshToken = output.refreshToken
        )
    }

    fun toSignInWithRefreshTokenInput(request: MemberRefreshTokenRequest): GenerateTokenWithRefreshTokenUseCase.Input {
        return GenerateTokenWithRefreshTokenUseCase.Input(
            refreshToken = request.refreshToken
        )
    }

    fun toSignInWithRefreshTokenResponse(output: GenerateTokenWithRefreshTokenUseCase.Output): OauthLoginResponse {
        return OauthLoginResponse(
            isRegistered = true,
            accessToken = output.accessToken,
            refreshToken = output.refreshToken,
            memberInfo = MemberResponse(
                memberId = output.member.id,
                name = output.member.name,
                oauthEmail = output.member.oauthEmail,
                role = output.member.role,
                provider = output.member.provider
            )
        )
    }
}
