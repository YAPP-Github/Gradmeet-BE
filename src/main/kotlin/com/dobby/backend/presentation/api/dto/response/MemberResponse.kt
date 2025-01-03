package com.dobby.backend.presentation.api.dto.response

import com.dobby.backend.domain.model.Member
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 DTO")
data class MemberResponse(
    @Schema(description = "사용자 ID", example = "1")
    val id: Long,

    @Schema(description = "OAuth 이메일", example = "dlawotn3@naver.com")
    val oauthEmail: String?,

    @Schema(description = "연락 이메일", example = "dlawotn3@naver.com")
    val contactEmail: String?,

    @Schema(description = "역할", example = "RESEARCHER")
    val role: RoleType?,

    @Schema(description = "이름", example = "야뿌")
    val name: String?,
) {

    companion object {
        fun fromDomain(member: Member): MemberResponse = with(member) {
            MemberResponse(
                id = memberId,
                oauthEmail = oauthEmail,
                contactEmail = contactEmail,
                role = role,
                name = name
            )
        }
    }
}
