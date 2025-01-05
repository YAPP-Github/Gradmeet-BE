package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.FetchGoogleUserInfoUseCase
import com.dobby.backend.application.usecase.FetchNaverUserInfoUseCase
import com.dobby.backend.presentation.api.dto.request.auth.GoogleOauthLoginRequest
import com.dobby.backend.presentation.api.dto.request.auth.NaverOauthLoginRequest
import com.dobby.backend.presentation.api.dto.response.auth.OauthLoginResponse
import org.springframework.stereotype.Service

@Service
class OauthService(
    private val fetchGoogleUserInfoUseCase: FetchGoogleUserInfoUseCase,
    private val fetchNaverUserInfoUseCase: FetchNaverUserInfoUseCase,
) {
    fun getGoogleUserInfo(oauthLoginRequest: GoogleOauthLoginRequest): OauthLoginResponse {
        return fetchGoogleUserInfoUseCase.execute(oauthLoginRequest)
    }

    fun getNaverUserInfo(oauthLoginRequest: NaverOauthLoginRequest): OauthLoginResponse {
        return fetchNaverUserInfoUseCase.execute(oauthLoginRequest)
    }
}
