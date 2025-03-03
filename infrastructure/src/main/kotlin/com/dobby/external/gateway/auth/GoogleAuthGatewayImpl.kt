package com.dobby.external.gateway.auth

import com.dobby.config.properties.GoogleAuthProperties
import com.dobby.external.feign.google.GoogleAuthFeignClient
import com.dobby.external.feign.google.GoogleUserInfoFeginClient
import com.dobby.gateway.auth.GoogleAuthGateway
import com.dobby.model.auth.GoogleToken
import com.dobby.model.auth.GoogleUserInfo
import org.springframework.stereotype.Component

@Component
class GoogleAuthGatewayImpl(
    private val googleAuthProperties: GoogleAuthProperties,
    private val googleAuthFeignClient: GoogleAuthFeignClient,
    private val googleUserInfoFeignClient: GoogleUserInfoFeginClient
): GoogleAuthGateway {

    override fun getAccessToken(code: String): GoogleToken {
        return googleAuthFeignClient.getAccessToken(
            clientId = googleAuthProperties.clientId,
            redirectUri = googleAuthProperties.redirectUri,
            code = code,
            clientSecret = googleAuthProperties.clientSecret,
            grantType = googleAuthProperties.grantType
        ).toDomain()
    }

    override fun getUserInfo(accessToken: String): GoogleUserInfo {
        return googleUserInfoFeignClient.getUserInfo("Bearer $accessToken")
            .toDomain()
    }
}
