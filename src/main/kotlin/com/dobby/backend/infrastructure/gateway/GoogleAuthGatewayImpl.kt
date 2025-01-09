package com.dobby.backend.infrastructure.gateway

import com.dobby.backend.domain.gateway.feign.GoogleAuthGateway
import com.dobby.backend.infrastructure.config.properties.GoogleAuthProperties
import com.dobby.backend.infrastructure.feign.google.GoogleAuthFeignClient
import com.dobby.backend.infrastructure.feign.google.GoogleUserInfoFeginClient
import com.dobby.backend.presentation.api.dto.request.auth.google.GoogleTokenRequest
import com.dobby.backend.presentation.api.dto.response.auth.google.GoogleInfoResponse
import com.dobby.backend.presentation.api.dto.response.auth.google.GoogleTokenResponse
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component

@Component
@EnableConfigurationProperties(GoogleAuthProperties::class)
class GoogleAuthGatewayImpl(
    private val googleAuthProperties: GoogleAuthProperties,
    private val googleAuthFeignClient: GoogleAuthFeignClient,
    private val googleUserInfoFeignClient: GoogleUserInfoFeginClient
): GoogleAuthGateway {

    override fun getAccessToken(code: String): GoogleTokenResponse {
        val googleTokenRequest = GoogleTokenRequest(
            code = code,
            clientId = googleAuthProperties.clientId,
            clientSecret = googleAuthProperties.clientSecret,
            redirectUri = googleAuthProperties.redirectUri
        )

        return googleAuthFeignClient.getAccessToken(googleTokenRequest)
    }

    override fun getUserInfo(token: String): GoogleInfoResponse {
        return googleUserInfoFeignClient.getUserInfo(token)
    }
}
