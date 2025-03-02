package com.dobby.backend.presentation.api.dto.request.member

import com.dobby.enums.member.WithdrawalReasonType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

data class DeleteMemberRequest (
    @NotNull(message = "탈퇴 사유 타입은 공백일 수 없습니다.")
    @Schema(description = "탈퇴 사유 타입")
    val reasonType: WithdrawalReasonType,

    @Schema(description = "기타 탈퇴 사유")
    val reason: String?
)
