package com.dobby.backend.domain.gateway.auth;

import com.dobby.backend.presentation.api.dto.response.auth.google.GoogleInfoResponse;
import com.dobby.backend.presentation.api.dto.response.auth.google.GoogleTokenResponse

interface GoogleAuthGateway {
    fun getAccessToken(code: String): GoogleTokenResponse
    fun getUserInfo(accessToken: String): GoogleInfoResponse
}
