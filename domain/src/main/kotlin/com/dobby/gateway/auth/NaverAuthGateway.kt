package com.dobby.gateway.auth

import com.dobby.model.auth.NaverToken
import com.dobby.model.auth.NaverUserInfo

interface NaverAuthGateway {
    fun getAccessToken(authorizationCode: String, state: String): NaverToken
    fun getUserInfo(accessToken: String): NaverUserInfo
}
