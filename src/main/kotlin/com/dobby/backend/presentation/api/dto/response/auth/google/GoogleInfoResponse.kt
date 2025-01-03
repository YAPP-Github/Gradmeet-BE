package com.dobby.backend.presentation.api.dto.response.auth.google

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleInfoResponse(
    @JsonProperty("email")
    val email: String,

    @JsonProperty("name")
    val name : String
)
