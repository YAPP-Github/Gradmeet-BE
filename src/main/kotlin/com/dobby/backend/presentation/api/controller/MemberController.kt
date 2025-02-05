package com.dobby.backend.presentation.api.controller

import com.dobby.backend.application.service.MemberService
import com.dobby.backend.presentation.api.dto.request.member.ParticipantSignupRequest
import com.dobby.backend.presentation.api.dto.request.member.ResearcherSignupRequest
import com.dobby.backend.presentation.api.dto.request.member.UpdateParticipantInfoRequest
import com.dobby.backend.presentation.api.dto.request.member.UpdateResearcherInfoRequest
import com.dobby.backend.presentation.api.dto.response.member.DefaultResponse
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
    private val memberService: MemberService,
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
    @GetMapping("/me/researchers")
    @Operation(
        summary = "연구자 회원 정보 렌더링",
        description = "연구자의 회원 정보를 반환합니다."
    )
    fun getResearcherInfo(): ResearcherInfoResponse {
        val input = MemberMapper.toGetResearcherInfoUseCaseInput()
        val output = memberService.getResearcherInfo(input)
        return MemberMapper.toResearcherInfoResponse(output)
    }

    @PreAuthorize("hasRole('RESEARCHER')")
    @PutMapping("/me/researchers")
    @Operation(
        summary = "연구자 회원 정보 수정",
        description = "연구자의 회원 정보를 수정합니다."
    )
    fun updateResearcherInfo(
        @RequestBody @Valid request: UpdateResearcherInfoRequest
    ): ResearcherInfoResponse {
        val input = MemberMapper.toUpdateResearcherInfoUseCaseInput(request)
        val output = memberService.updateResearcherInfo(input)
        return MemberMapper.toResearcherInfoResponse(output)
    }

    @PreAuthorize("hasRole('PARTICIPANT')")
    @GetMapping("/me/participants")
    @Operation(
        summary = "참여자 회원 정보 렌더링",
        description = "참여자의 회원 정보를 반환합니다."
    )
    fun getParticipantInfo(): ParticipantInfoResponse {
        val input = MemberMapper.toGetParticipantInfoUseCaseInput()
        val output = memberService.getParticipantInfo(input)
        return MemberMapper.toParticipantInfoResponse(output)
    }

    @PreAuthorize("hasRole('PARTICIPANT')")
    @PutMapping("/me/participants")
    @Operation(
        summary = "참여자 회원 정보 수정",
        description = "참여자의 회원 정보를 수정합니다."
    )
    fun updateParticipantInfo(
        @RequestBody @Valid request: UpdateParticipantInfoRequest
    ): ParticipantInfoResponse {
        val input = MemberMapper.toUpdateParticipantInfoUseCaseInput(request)
        val output = memberService.updateParticipantInfo(input)
        return MemberMapper.toParticipantInfoResponse(output)
    }

    @GetMapping("/me/validate/contact-email")
    @Operation(
        summary = "연락 받을 이메일 주소 검증 API",
        description = "회원 정보 수정 시, 이메일 중복 확인을 위한 API입니다."
    )
    fun validateContactEmailForUpdate(
        @RequestParam email: String
    ): DefaultResponse {
        val input = MemberMapper.toValidateContactEmailForUpdateUseCaseInput(email)
        val output = memberService.validateContactEmailForUpdate(input)
        return DefaultResponse(output.isDuplicate)
    }
}
