package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.FetchGoogleUserInfoUseCase
import com.dobby.backend.domain.exception.OAuth2EmailNotFoundException
import com.dobby.backend.domain.exception.OAuth2NameNotFoundException
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.presentation.api.dto.request.GoogleTokenRequest
import com.dobby.backend.presentation.api.dto.request.OauthLoginRequest
import com.dobby.backend.presentation.api.dto.response.OauthLoginResponse
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class OauthService(
    private val fetchGoogleUserInfoUseCase: FetchGoogleUserInfoUseCase
) {
    fun getGoogleUserInfo(oauthLoginRequest: OauthLoginRequest): OauthLoginResponse {
        return fetchGoogleUserInfoUseCase.execute(oauthLoginRequest)
    }

}