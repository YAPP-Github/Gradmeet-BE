package com.dobby.backend.presentation.api.dto.response.auth.google

import com.dobby.domain.model.auth.GoogleUserInfo
import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleInfoResponse(
    @JsonProperty("email")
    val email: String
) {
    fun toDomain(): GoogleUserInfo = GoogleUserInfo(email)
}
