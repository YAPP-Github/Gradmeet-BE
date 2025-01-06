package com.dobby.backend.presentation.api.dto.response.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class NaverInfoResponse(
    @JsonProperty("email")
    val email: String,

    @JsonProperty("name")
    val name: String
)
