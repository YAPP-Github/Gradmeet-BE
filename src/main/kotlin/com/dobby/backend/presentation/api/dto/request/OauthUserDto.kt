package com.dobby.backend.presentation.api.dto.request

import com.dobby.backend.infrastructure.database.entity.enum.ProviderType

data class OauthUserDto(
    val email: String,
    val name: String,
    val provider: ProviderType
)
