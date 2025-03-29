package com.dobby.api.dto.response.auth.google

import com.dobby.model.auth.GoogleToken
import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String
) {
    fun toDomain(): GoogleToken = GoogleToken(accessToken)
}
