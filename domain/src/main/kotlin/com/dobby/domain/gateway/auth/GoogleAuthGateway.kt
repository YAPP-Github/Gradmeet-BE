package com.dobby.domain.gateway.auth;

import com.dobby.domain.model.auth.GoogleToken
import com.dobby.domain.model.auth.GoogleUserInfo

interface GoogleAuthGateway {
    fun getAccessToken(code: String): GoogleToken
    fun getUserInfo(accessToken: String): GoogleUserInfo
}
