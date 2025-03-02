package com.dobby.gateway.auth;

import com.dobby.model.auth.GoogleToken
import com.dobby.model.auth.GoogleUserInfo

interface GoogleAuthGateway {
    fun getAccessToken(code: String): GoogleToken
    fun getUserInfo(accessToken: String): GoogleUserInfo
}
