package com.dobby.backend.presentation.api.controller

import com.dobby.backend.application.service.SignupService
import com.dobby.backend.presentation.api.dto.request.signup.ParticipantSignupRequest
import com.dobby.backend.presentation.api.dto.request.signup.ResearcherSignupRequest
import com.dobby.backend.presentation.api.dto.response.signup.SignupResponse
import com.dobby.backend.presentation.api.mapper.SignupMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@Tag(name = "회원가입 API - /v1/members/signup")
@RestController
@RequestMapping("/v1/members")
class SignupController(
    private val signupService: SignupService
) {
    @PostMapping("/signup/participant")
    @Operation(
        summary = "참여자 회원가입 API- OAuth 로그인 필수",
        description = "참여자 OAuth 로그인 실패 시, 리다이렉팅하여 참여자 회원가입하는 API입니다."
    )
    fun signupParticipants(
        @RequestBody @Valid req: ParticipantSignupRequest
    ): SignupResponse {
        val input = SignupMapper.toCreateParticipantInput(req)
        val output = signupService.participantSignup(input)
        return SignupMapper.toParticipantSignupResponse(output)
    }

    @PostMapping("/signup/researcher")
    @Operation(
        summary = "연구자 회원가입 API - OAuth 로그인 필수",
        description = """
         연구자 OAuth 로그인 실패 시, 리다이렉팅하여 연구자 회원가입하는 API입니다.
         ⚠️ 이메일 인증 성공 시에만 회원가입이 완료되어야 합니다.
            """
    )
    fun signupResearchers(
        @RequestBody @Valid req: ResearcherSignupRequest
    ): SignupResponse {
        val input = SignupMapper.toCreateResearcherInput(req)
        val output = signupService.researcherSignup(input)
        return SignupMapper.toResearcherSignupResponse(output)
    }
}
