package com.dobby.backend.infrastructure.gateway

import com.dobby.backend.domain.gateway.feign.GoogleAuthGateway
import com.dobby.backend.infrastructure.config.properties.GoogleAuthProperties
import com.dobby.backend.infrastructure.feign.google.GoogleAuthFeignClient
import com.dobby.backend.infrastructure.feign.google.GoogleUserInfoFeginClient
import com.dobby.backend.presentation.api.dto.response.auth.google.GoogleInfoResponse
import com.dobby.backend.presentation.api.dto.response.auth.google.GoogleTokenResponse
import org.springframework.stereotype.Component

@Component
class GoogleAuthGatewayImpl(
    private val googleAuthProperties: GoogleAuthProperties,
    private val googleAuthFeignClient: GoogleAuthFeignClient,
    private val googleUserInfoFeignClient: GoogleUserInfoFeginClient
): GoogleAuthGateway {

    override fun getAccessToken(code: String): GoogleTokenResponse {
        return googleAuthFeignClient.getAccessToken(
            clientId = googleAuthProperties.clientId,
            redirectUri = googleAuthProperties.redirectUri,
            code = code,
            clientSecret = googleAuthProperties.clientSecret,
            grantType = googleAuthProperties.grantType
        )
    }

    override fun getUserInfo(accessToken: String): GoogleInfoResponse {
        return googleUserInfoFeignClient.getUserInfo("Bearer $accessToken")
    }
}
