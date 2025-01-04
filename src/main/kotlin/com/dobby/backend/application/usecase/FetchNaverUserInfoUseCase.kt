package com.dobby.backend.application.usecase

import com.dobby.backend.application.mapper.OauthUserMapper
import com.dobby.backend.domain.exception.OAuth2EmailNotFoundException
import com.dobby.backend.domain.exception.SignInMemberException
import com.dobby.backend.infrastructure.config.properties.NaverAuthProperties
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.repository.MemberRepository
import com.dobby.backend.infrastructure.feign.NaverAuthFeignClient
import com.dobby.backend.infrastructure.feign.NaverUserInfoFeignClient
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.presentation.api.dto.request.NaverTokenRequest
import com.dobby.backend.presentation.api.dto.request.OauthLoginRequest
import com.dobby.backend.presentation.api.dto.response.auth.NaverTokenResponse
import com.dobby.backend.presentation.api.dto.response.auth.OauthLoginResponse
import com.dobby.backend.util.AuthenticationUtils

class FetchNaverUserInfoUseCase(
    private val naverAuthFeignClient: NaverAuthFeignClient,
    private val naverUserInfoFeginClient: NaverUserInfoFeignClient,
    private val jwtTokenProvider: JwtTokenProvider,
    private val naverAuthProperties: NaverAuthProperties,
    private val memberRepository: MemberRepository
) : UseCase<OauthLoginRequest, OauthLoginResponse> {

    override fun execute(input: OauthLoginRequest): OauthLoginResponse {
        try {
            val naverTokenRequest = NaverTokenRequest(
                grantType = "authorization_code",
                clientId = naverAuthProperties.clientId,
                clientSecret = naverAuthProperties.clientSecret,
                code = input.authorizationCode,
                state = input.state
            )

            val oauthRes = fetchAccessToken(naverTokenRequest)
            val oauthToken = oauthRes.accessToken

            val userInfo = naverUserInfoFeginClient.getUserInfo("Bearer $oauthToken")
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
                provider = ProviderType.NAVER
            )
        } catch (e: SignInMemberException) {
            throw SignInMemberException()
        }
    }

    private fun fetchAccessToken(naverTokenRequest: NaverTokenRequest): NaverTokenResponse {
        return naverAuthFeignClient.getAccessToken(naverTokenRequest.grantType,
            naverTokenRequest.clientId, naverTokenRequest.clientSecret, naverTokenRequest.code,
            naverTokenRequest.state)
    }
}
