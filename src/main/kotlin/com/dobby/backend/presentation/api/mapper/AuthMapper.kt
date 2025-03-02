package com.dobby.backend.presentation.api.mapper

import com.dobby.usecase.auth.FetchGoogleUserInfoUseCase
import com.dobby.usecase.auth.FetchNaverUserInfoUseCase
import com.dobby.usecase.auth.GenerateTestTokenUseCase
import com.dobby.usecase.auth.GenerateTokenWithRefreshTokenUseCase
import com.dobby.enums.member.RoleType
import com.dobby.backend.presentation.api.dto.response.auth.OauthLoginResponse
import com.dobby.backend.presentation.api.dto.response.auth.TestMemberSignInResponse
import com.dobby.backend.presentation.api.dto.response.member.MemberResponse
import com.dobby.backend.presentation.api.dto.request.auth.GoogleOauthLoginRequest
import com.dobby.backend.presentation.api.dto.request.auth.MemberRefreshTokenRequest
import com.dobby.backend.presentation.api.dto.request.auth.NaverOauthLoginRequest

object AuthMapper {
    fun toGoogleOauthLoginInput(request: GoogleOauthLoginRequest, role: RoleType): FetchGoogleUserInfoUseCase.Input {
        return FetchGoogleUserInfoUseCase.Input(
            authorizationCode = request.authorizationCode,
            role = role
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
                provider = output.provider,
                contactEmail = output.contactEmail
            )
        )
    }

    fun toNaverOauthLoginInput(request: NaverOauthLoginRequest, role: RoleType
    ): FetchNaverUserInfoUseCase.Input {
        return FetchNaverUserInfoUseCase.Input(
            authorizationCode = request.authorizationCode,
            state = request.state,
            role = role
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
                provider = output.provider,
                contactEmail = output.contactEmail
            )
        )
    }

    fun toForceTokenInput(input: String): GenerateTestTokenUseCase.Input {
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
                provider = output.member.provider,
                contactEmail = output.member.contactEmail
            )
        )
    }
}
