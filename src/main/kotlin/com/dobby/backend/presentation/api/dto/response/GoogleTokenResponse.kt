package com.dobby.backend.presentation.api.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleTokenResponse (
    @JsonProperty("access_token")
    val accessToken: String
)