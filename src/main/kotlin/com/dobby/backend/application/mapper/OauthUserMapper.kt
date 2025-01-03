package com.dobby.backend.application.mapper

import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.presentation.api.dto.response.MemberResponse
import com.dobby.backend.presentation.api.dto.response.OauthLoginResponse

object OauthUserMapper {
    fun toDto(
        isRegistered: Boolean,
        accessToken: String,
        refreshToken: String,
        oauthEmail: String,
        oauthName: String,
        role: RoleType,
        provider: ProviderType,
        memberId: Long? = null
    ): OauthLoginResponse {
        return OauthLoginResponse(
            isRegistered = isRegistered,
            accessToken = accessToken,
            refreshToken = refreshToken,
            memberInfo = MemberResponse(
                memberId = memberId,
                oauthEmail = oauthEmail,
                name = oauthName,
                role = role,
                provider = provider
            )
        )
    }
}
