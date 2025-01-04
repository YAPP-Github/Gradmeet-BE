package com.dobby.backend.presentation.api.dto.response.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class NaverTokenResponse (
    @JsonProperty("access_token")
    val accessToken: String
)