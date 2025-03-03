package com.dobby.api.dto.response.auth.google

import com.dobby.model.auth.GoogleUserInfo
import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleInfoResponse(
    @JsonProperty("email")
    val email: String
) {
    fun toDomain(): GoogleUserInfo = GoogleUserInfo(email)
}
