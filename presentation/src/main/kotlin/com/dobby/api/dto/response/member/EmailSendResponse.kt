package com.dobby.api.dto.response.member

import io.swagger.v3.oas.annotations.media.Schema

data class EmailSendResponse(
    @Schema(description = "학교 이메일 인증을 성공하여, 코드를 성공적으로 전송했는지 여부입니다.")
    val isSuccess: Boolean,

    @Schema(description = "반환 성공 메시지 입니다.")
    val message : String
)
