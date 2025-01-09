package com.dobby.backend.infrastructure.gateway

import com.dobby.backend.domain.gateway.feign.NaverAuthGateway
import com.dobby.backend.infrastructure.config.properties.NaverAuthProperties
import com.dobby.backend.infrastructure.feign.naver.NaverAuthFeignClient
import com.dobby.backend.infrastructure.feign.naver.NaverUserInfoFeignClient
import com.dobby.backend.presentation.api.dto.request.auth.NaverTokenRequest
import com.dobby.backend.presentation.api.dto.response.auth.naver.NaverInfoResponse
import com.dobby.backend.presentation.api.dto.response.auth.naver.NaverTokenResponse
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component

@Component
@EnableConfigurationProperties(NaverAuthProperties::class)
class NaverAuthGatewayImpl(
    private val naverAuthProperties: NaverAuthProperties,
    private val naverAuthFeignClient: NaverAuthFeignClient,
    private val naverUserInfoFeignClient: NaverUserInfoFeignClient
): NaverAuthGateway {

    override fun getAccessToken(authorizationCode: String, state: String): String? {
        val naverTokenRequest = NaverTokenRequest(
            grantType = "authorization_code",
            clientId = naverAuthProperties.clientId,
            clientSecret = naverAuthProperties.clientSecret,
            code = authorizationCode,
            state = state
        )

        val tokenResponse: NaverTokenResponse = naverAuthFeignClient.getAccessToken(naverTokenRequest)
        return tokenResponse.accessToken
    }

    override fun getUserInfo(accessToken: String): NaverInfoResponse? {
        return naverUserInfoFeignClient.getUserInfo("Bearer $accessToken")
    }
}
