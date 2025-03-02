package com.dobby.domain.gateway.auth

import com.dobby.domain.model.auth.NaverToken
import com.dobby.domain.model.auth.NaverUserInfo

interface NaverAuthGateway {
    fun getAccessToken(authorizationCode: String, state: String): NaverToken
    fun getUserInfo(accessToken: String): NaverUserInfo
}
