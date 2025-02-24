package com.dobby.backend.presentation.api.dto.response.member

import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.enums.member.ProviderType
import com.dobby.backend.domain.enums.member.RoleType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 DTO")
data class MemberResponse(
    @Schema(description = "사용자 ID", example = "01HGXN4H4PXNRH4")
    val memberId: String?,

    @Schema(description = "이름", example = "야뿌")
    val name: String?,

    @Schema(description = "OAuth 이메일", example = "dlawotn3@naver.com")
    val oauthEmail: String?,

    @Schema(description = "OAuth 제공자", example = "GOOGLE")
    val provider: ProviderType?,

    @Schema(description = "연락받을 이메일", example = "dlawotn3@naver.com")
    val contactEmail: String?,

    @Schema(description = "역할", example = "RESEARCHER")
    val role: RoleType?,
) {

    companion object {
        fun fromDomain(member: Member): MemberResponse = with(member) {
            MemberResponse(
                memberId = id,
                oauthEmail = oauthEmail,
                provider = provider,
                contactEmail = contactEmail,
                role = role,
                name = name
            )
        }
    }
}
