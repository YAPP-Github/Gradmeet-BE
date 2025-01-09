package com.dobby.backend.domain.gateway.feign;

import com.dobby.backend.presentation.api.dto.response.auth.google.GoogleInfoResponse;
import com.dobby.backend.presentation.api.dto.response.auth.google.GoogleTokenResponse

interface GoogleAuthGateway {
    fun getAccessToken(code: String): GoogleTokenResponse
    fun getUserInfo(token: String): GoogleInfoResponse
}
