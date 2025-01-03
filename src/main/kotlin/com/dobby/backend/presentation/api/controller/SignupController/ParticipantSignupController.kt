package com.dobby.backend.presentation.api.controller.SignupController

import com.dobby.backend.application.service.SignupService
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType.*
import com.dobby.backend.presentation.api.dto.request.ParticipantSignupRequest
import com.dobby.backend.presentation.api.dto.response.signup.SignupResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@Tag(name = "회원가입 API")
@RestController
@RequestMapping("/v1/participants")
class ParticipantSignupController(
    private val signupService: SignupService
) {
    @PostMapping("/signup")
    @Operation(
        summary = "참여자 회원가입 API- OAuth 로그인 필수",
        description = "참여자 OAuth 로그인 실패 시, 리다이렉팅하여 참여자 회원가입하는 API입니다."
    )
    fun signup(
        @RequestBody @Valid req: ParticipantSignupRequest
    ): SignupResponse {
        return signupService.participantSignup(req)
    }
}
