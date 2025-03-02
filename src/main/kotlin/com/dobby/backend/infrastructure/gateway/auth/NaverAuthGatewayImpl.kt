package com.dobby.backend.infrastructure.gateway.auth

import com.dobby.domain.gateway.auth.NaverAuthGateway
import com.dobby.backend.infrastructure.config.properties.NaverAuthProperties
import com.dobby.backend.infrastructure.feign.naver.NaverAuthFeignClient
import com.dobby.backend.infrastructure.feign.naver.NaverUserInfoFeignClient
import com.dobby.backend.presentation.api.dto.response.auth.naver.NaverInfoResponse
import com.dobby.backend.presentation.api.dto.response.auth.naver.NaverTokenResponse
import com.dobby.domain.model.auth.NaverToken
import com.dobby.domain.model.auth.NaverUserInfo
import org.springframework.stereotype.Component

@Component
class NaverAuthGatewayImpl(
    private val naverAuthProperties: NaverAuthProperties,
    private val naverAuthFeignClient: NaverAuthFeignClient,
    private val naverUserInfoFeignClient: NaverUserInfoFeignClient
): NaverAuthGateway {

    override fun getAccessToken(authorizationCode: String, state: String): NaverToken {
        return naverAuthFeignClient.getAccessToken(
            code = authorizationCode,
            clientId = naverAuthProperties.clientId,
            clientSecret = naverAuthProperties.clientSecret,
            state = state,
            grantType = "authorization_code"
        ).toDomain()
    }

    override fun getUserInfo(accessToken: String): NaverUserInfo {
        return naverUserInfoFeignClient.getUserInfo("Bearer $accessToken")
            .toDomain()
    }
}
