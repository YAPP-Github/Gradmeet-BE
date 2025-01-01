package com.dobby.backend.application.mapper

import com.dobby.backend.domain.exception.OAuth2EmailNotFoundException
import com.dobby.backend.domain.exception.OAuth2NameNotFoundException
import com.dobby.backend.infrastructure.database.entity.Member
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.presentation.api.dto.request.OauthUserDto
import org.springframework.security.oauth2.core.user.OAuth2User

object OauthUserMapper {
    fun toDto(oauthUser: OAuth2User, provider: ProviderType): OauthUserDto {
        val email = oauthUser.getAttribute<String>("email")
            ?: throw OAuth2EmailNotFoundException()
        val name = oauthUser.getAttribute<String>("name")
            ?: throw OAuth2NameNotFoundException()

        return OauthUserDto(email=email, name=name, provider= provider)
    }

    fun toTempMember(dto: OauthUserDto): Member {
        return Member(
            id = 0L,
            oauthEmail= dto.email,
            name= dto.name,
            provider = dto.provider,
            status = MemberStatus.HOLD,
            role = null,
            contactEmail = null,
            birthDate = null
        )
    }
}
