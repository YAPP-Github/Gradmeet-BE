package com.dobby.backend.domain.gateway.auth

import com.dobby.backend.presentation.api.dto.response.auth.naver.NaverInfoResponse
import com.dobby.backend.presentation.api.dto.response.auth.naver.NaverTokenResponse

interface NaverAuthGateway {
    fun getAccessToken(authorizationCode: String, state: String): NaverTokenResponse
    fun getUserInfo(accessToken: String): NaverInfoResponse?
}
