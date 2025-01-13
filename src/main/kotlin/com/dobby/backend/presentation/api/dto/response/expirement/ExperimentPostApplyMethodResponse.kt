package com.dobby.backend.presentation.api.dto.response.expirement

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "실험 공고 참여 방법 조회 응답 DTO")
data class ExperimentPostApplyMethodResponse(
    @Schema(description = "참여 방법 Id", example = "1")
    val applyMethodId: Long,

    @Schema(description = "전화번호", example = "010-1234-5678")
    val phoneNum: String?,

    @Schema(description = "신청서 URL", example = "https://form.com")
    val formUrl: String?,

    @Schema(description = "참여 방법 내용", example = "해당 전화번호로 연락주세요.")
    val content: String
)
