package com.dobby.backend.presentation.api.dto.response

import com.dobby.backend.infrastructure.database.entity.enum.ProviderType

data class OauthTokenResponse(
    val jwtToken: String,
    val email: String,
    val name: String,
    val provider: ProviderType
)
