package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.FetchGoogleUserInfoUseCase
import com.dobby.backend.domain.exception.OAuth2EmailNotFoundException
import com.dobby.backend.domain.exception.OAuth2NameNotFoundException
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.presentation.api.dto.response.OauthTokenResponse
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class OauthService(
    private val fetchGoogleUserInfoUseCase: FetchGoogleUserInfoUseCase
) {
    fun getGoogleUserInfo(accessToken: String): OauthTokenResponse {
        val userInfo = fetchGoogleUserInfoUseCase.execute(accessToken)

        val email = userInfo["email"] as? String ?: throw OAuth2EmailNotFoundException()
        val name = userInfo["name"] as? String ?: throw OAuth2NameNotFoundException()
        return OauthTokenResponse(
            jwtToken= accessToken,
            email = email,
            name = name,
            provider = ProviderType.GOOGLE
        )
    }
}