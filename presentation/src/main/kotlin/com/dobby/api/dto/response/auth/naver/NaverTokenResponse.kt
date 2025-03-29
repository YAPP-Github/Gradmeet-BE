package com.dobby.api.dto.response.auth.naver

import com.dobby.model.auth.NaverToken
import com.fasterxml.jackson.annotation.JsonProperty

data class NaverTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String
) {
    fun toDomain(): NaverToken = NaverToken(accessToken)
}
