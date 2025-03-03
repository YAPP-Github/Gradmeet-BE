package com.dobby.external.gateway.auth

import com.dobby.config.properties.NaverAuthProperties
import com.dobby.feign.naver.NaverAuthFeignClient
import com.dobby.feign.naver.NaverUserInfoFeignClient
import com.dobby.model.auth.NaverToken
import com.dobby.model.auth.NaverUserInfo
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
