package com.dobby.backend.domain.gateway.feign

import com.dobby.backend.presentation.api.dto.response.auth.naver.NaverInfoResponse

interface NaverAuthGateway {
    fun getAccessToken(authorizationCode: String, state: String): String?
    fun getUserInfo(accessToken: String): NaverInfoResponse?
}
