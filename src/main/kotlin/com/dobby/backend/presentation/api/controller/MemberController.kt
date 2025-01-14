package com.dobby.backend.presentation.api.controller

import com.dobby.backend.application.service.MemberService
import com.dobby.backend.presentation.api.dto.request.signup.ParticipantSignupRequest
import com.dobby.backend.presentation.api.dto.request.signup.ResearcherSignupRequest
import com.dobby.backend.presentation.api.dto.response.member.ParticipantInfoResponse
import com.dobby.backend.presentation.api.dto.response.member.ResearcherInfoResponse
import com.dobby.backend.presentation.api.dto.response.member.SignupResponse
import com.dobby.backend.presentation.api.mapper.MemberMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Tag(name = "회원 API - /v1/members")
@RestController
@RequestMapping("/v1/members")
class MemberController(
    private val memberService: MemberService
) {
    @PostMapping("/signup/participant")
    @Operation(
        summary = "참여자 회원가입 API- OAuth 로그인 필수",
        description = "참여자 OAuth 로그인 실패 시, 리다이렉팅하여 참여자 회원가입하는 API입니다."
    )
    fun signupParticipants(
        @RequestBody @Valid req: ParticipantSignupRequest
    ): SignupResponse {
        val input = MemberMapper.toCreateParticipantInput(req)
        val output = memberService.participantSignup(input)
        return MemberMapper.toParticipantSignupResponse(output)
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
        val input = MemberMapper.toCreateResearcherInput(req)
        val output = memberService.researcherSignup(input)
        return MemberMapper.toResearcherSignupResponse(output)
    }

    @PreAuthorize("hasRole('RESEARCHER')")
    @GetMapping("/researchers")
    @Operation(
        summary = "연구자 기본 정보 렌더링",
        description = "연구자의 기본 정보 [학교 + 전공 + 랩실 정보 + 이름]를 반환합니다."
    )
    fun getResearcherInfo(): ResearcherInfoResponse {
        val input = MemberMapper.toGetResearcherInfoUseCaseInput()
        val output = memberService.getResearcherInfo(input)
        return MemberMapper.toResearcherInfoResponse(output)
    }

    @PreAuthorize("hasRole('PARTICIPANT')")
    @GetMapping("/participants")
    @Operation(
        summary = "참여자 회원 기본 정보 렌더링",
        description = "참여자의 기본 정보를 반환합니다."
    )
    fun getParticipantInfo(): ParticipantInfoResponse {
        val input = MemberMapper.toGetParticipantInfoUseCaseInput()
        val output = memberService.getParticipantInfo(input)
        return MemberMapper.toParticipantInfoResponse(output)
    }
}
