package com.dobby.backend.presentation.api.dto.response

import io.swagger.v3.oas.annotations.media.Schema

<<<<<<< HEAD
@Schema(description = "로그인 결과 DTO")
data class MemberSignInResponse(
=======
@Schema(description = "테스트용 로그인 결과 DTO")
data class TestMemberSignInResponse(
>>>>>>> 2c43662 (feat: add GenerateTestToken api for test member)
    @Schema(description = "엑세스 토큰")
    val accessToken: String,

    @Schema(description = "리프레쉬 토큰")
<<<<<<< HEAD
    val refreshToken: String,

    @Schema(description = "사용자 정보")
    val member: MemberResponse,
) {
}
=======
    val refreshToken: String
)
>>>>>>> 2c43662 (feat: add GenerateTestToken api for test member)
