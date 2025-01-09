package com.dobby.backend.presentation.api.dto.response.auth.naver

import com.fasterxml.jackson.annotation.JsonProperty

data class NaverInfoResponse(
    @JsonProperty("email")
    val email: String
)
