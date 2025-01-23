package com.dobby.backend.presentation.api.controller

import com.dobby.backend.application.service.MemberService
import com.dobby.backend.application.service.PaginationService
import com.dobby.backend.presentation.api.dto.response.PaginatedResponse
import com.dobby.backend.presentation.api.dto.response.member.MyExperimentPostResponse
import com.dobby.backend.presentation.api.dto.request.member.ParticipantSignupRequest
import com.dobby.backend.presentation.api.dto.request.member.ResearcherSignupRequest
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
    private val paginationService: PaginationService
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
    @GetMapping("/researchers/me")
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
    @GetMapping("/participants/me")
    @Operation(
        summary = "참여자 회원 기본 정보 렌더링",
        description = "참여자의 기본 정보를 반환합니다."
    )
    fun getParticipantInfo(): ParticipantInfoResponse {
        val input = MemberMapper.toGetParticipantInfoUseCaseInput()
        val output = memberService.getParticipantInfo(input)
        return MemberMapper.toParticipantInfoResponse(output)
    }

    @PreAuthorize("hasRole('RESEARCHER')")
    @GetMapping("/researchers/me/experiment-posts")
    @Operation(
        summary = "연구자가 작성한 실험 공고 리스트 조회",
        description = "로그인한 연구자가 작성한 실험 공고 리스트를 반환합니다"
    )
    fun getMyExperimentPosts(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "6") count: Int,
        @RequestParam(defaultValue = "DESC") order: String
    ): PaginatedResponse<MyExperimentPostResponse> {
        val pagination = MemberMapper.toUseCasePagination(page, count, order)
        val input = MemberMapper.toGetMyExperimentPosts(pagination)
        val posts = memberService.getMyExperimentPosts(input)

        val totalCountInput = MemberMapper.toGetTotalMyExperimentPostCountUseCaseInput()
        val totalCount = memberService.getMyExperimentPostsCount(totalCountInput).totalPostCount
        val isLast = paginationService.isLastPage(totalCount, count, page)
        return MemberMapper.toGetMyExperimentPostsResponse(posts, page, totalCount, isLast)
    }

    @PreAuthorize("hasRole('RESEARCHER')")
    @PatchMapping("/researchers/me/experiment-posts/{postId}/recruit-status")
    @Operation(
        summary = "연구자가 작성한 특정 실험 공고 모집 상태 수정",
        description = "로그인한 연구자가 작성한 특정 실험 공고의 모집 상태를 변경합니다"
    )
    fun updateMyExperimentPostRecruitStatus(
        @PathVariable postId: Long
    ): MyExperimentPostResponse {
        val input = MemberMapper.toUpdateMyExperimentPostRecruitStatusUseCaseInput(postId)
        val output = memberService.updateMyExperimentPostRecruitStatus(input)
        return MemberMapper.toMyExperimentPostResponse(output)
    }
}
