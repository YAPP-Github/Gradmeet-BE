package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.FetchGoogleUserInfoUseCase
import com.dobby.backend.application.usecase.FetchNaverUserInfoUseCase
import com.dobby.backend.presentation.api.dto.request.OauthLoginRequest
import com.dobby.backend.presentation.api.dto.response.auth.OauthLoginResponse
import org.springframework.stereotype.Service

@Service
class OauthService(
    private val fetchGoogleUserInfoUseCase: FetchGoogleUserInfoUseCase,
    private val fetchNaverUserInfoUseCase: FetchNaverUserInfoUseCase,
) {
    fun getGoogleUserInfo(oauthLoginRequest: OauthLoginRequest): OauthLoginResponse {
        return fetchGoogleUserInfoUseCase.execute(oauthLoginRequest)
    }

    fun getNaverUserInfo(oauthLoginRequest: OauthLoginRequest): OauthLoginResponse {
        return fetchNaverUserInfoUseCase.execute(oauthLoginRequest)
    }
}
