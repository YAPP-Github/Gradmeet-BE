package com.dobby.backend.presentation.api.dto.response.auth.naver

import com.dobby.domain.model.auth.NaverUserInfo
import com.fasterxml.jackson.annotation.JsonProperty

data class NaverInfoResponse(
    @JsonProperty("resultcode")
    val resultCode: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("response")
    val response: NaverUserResponse
) {
    fun toDomain(): NaverUserInfo = response.toDomain()
}

data class NaverUserResponse(
    @JsonProperty("email")
    val email: String
) {
    fun toDomain(): NaverUserInfo = NaverUserInfo(email)
}
