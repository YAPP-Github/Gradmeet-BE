package com.dobby.backend.presentation.api.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class NaverTokenRequest (
    @JsonProperty("grant_type")
    val grantType: String = "authorization_code",

    @JsonProperty("client_id")
    val clientId: String,

    @JsonProperty("client_secret")
    val clientSecret: String,

    @JsonProperty("code")
    val code: String,

    @JsonProperty("state")
    val state: String
)