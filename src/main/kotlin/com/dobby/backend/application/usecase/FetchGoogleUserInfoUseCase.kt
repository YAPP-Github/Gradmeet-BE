package com.dobby.backend.application.usecase

import com.dobby.backend.application.mapper.OauthUserMapper
import com.dobby.backend.domain.exception.SignInMemberException
import com.dobby.backend.infrastructure.config.properties.GoogleAuthProperties
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.repository.MemberRepository
import com.dobby.backend.infrastructure.feign.GoogleAuthFeignClient
import com.dobby.backend.infrastructure.feign.GoogleUserInfoFeginClient
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.presentation.api.dto.request.GoogleOauthLoginRequest
import com.dobby.backend.presentation.api.dto.request.GoogleTokenRequest
import com.dobby.backend.presentation.api.dto.response.auth.GoogleTokenResponse
import com.dobby.backend.presentation.api.dto.response.auth.OauthLoginResponse
import com.dobby.backend.util.AuthenticationUtils

class FetchGoogleUserInfoUseCase(
    private val googleAuthFeignClient: GoogleAuthFeignClient,
    private val googleUserInfoFeginClient: GoogleUserInfoFeginClient,
    private val jwtTokenProvider: JwtTokenProvider,
    private val googleAuthProperties: GoogleAuthProperties,
    private val memberRepository: MemberRepository
) : UseCase<GoogleOauthLoginRequest, OauthLoginResponse> {

    override fun execute(input: GoogleOauthLoginRequest): OauthLoginResponse {
        try {
            val googleTokenRequest = GoogleTokenRequest(
                code = input.authorizationCode,
                clientId = googleAuthProperties.clientId,
                clientSecret = googleAuthProperties.clientSecret,
                redirectUri = googleAuthProperties.redirectUri
            )

            val oauthRes = fetchAccessToken(googleTokenRequest)
            val oauthToken = oauthRes.accessToken

            val userInfo = googleUserInfoFeginClient.getUserInfo("Bearer $oauthToken")
            val email = userInfo.email
            val regMember = memberRepository.findByOauthEmailAndStatus(email, MemberStatus.ACTIVE)
                ?: throw SignInMemberException()

            val regMemberAuthentication = AuthenticationUtils.createAuthentication(regMember)
            val jwtAccessToken = jwtTokenProvider.generateAccessToken(regMemberAuthentication)
            val jwtRefreshToken = jwtTokenProvider.generateRefreshToken(regMemberAuthentication)

            return OauthUserMapper.toDto(
                isRegistered = true,
                accessToken = jwtAccessToken,
                refreshToken = jwtRefreshToken,
                oauthEmail = regMember.oauthEmail,
                oauthName = regMember.name ?: throw SignInMemberException(),
                role = regMember.role ?: throw SignInMemberException(),
                provider = ProviderType.GOOGLE
            )
        } catch (e: SignInMemberException) {
            throw SignInMemberException()
        }
    }

    private fun fetchAccessToken(googleTokenRequest: GoogleTokenRequest): GoogleTokenResponse {
        return googleAuthFeignClient.getAccessToken(googleTokenRequest)
    }
}
