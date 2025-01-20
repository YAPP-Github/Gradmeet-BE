package com.dobby.backend.presentation.api.dto.response.auth.naver

import com.fasterxml.jackson.annotation.JsonProperty

data class NaverInfoResponse(
    @JsonProperty("resultcode")
    val resultCode: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("response")
    val response: NaverUserResponse
)

data class NaverUserResponse(
    @JsonProperty("email")
    val email: String
)
