package com.dobby.backend.presentation.api.controller

import com.dobby.backend.application.service.ExperimentPostService
import com.dobby.backend.application.service.PaginationService
import com.dobby.backend.presentation.api.dto.request.PreSignedUrlRequest
import com.dobby.backend.presentation.api.dto.response.PreSignedUrlResponse
import com.dobby.backend.infrastructure.database.entity.enums.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import com.dobby.backend.infrastructure.database.entity.enums.experiment.RecruitStatus
import com.dobby.backend.presentation.api.dto.request.experiment.CreateExperimentPostRequest
import com.dobby.backend.presentation.api.dto.response.PaginatedResponse
import com.dobby.backend.presentation.api.dto.response.experiment.*
import com.dobby.backend.presentation.api.dto.response.experiment.CreateExperimentPostResponse
import com.dobby.backend.presentation.api.dto.response.experiment.ExperimentPostApplyMethodResponse
import com.dobby.backend.presentation.api.dto.response.experiment.ExperimentPostCountsResponse
import com.dobby.backend.presentation.api.dto.response.experiment.ExperimentPostDetailResponse
import com.dobby.backend.presentation.api.dto.response.member.MyExperimentPostResponse
import com.dobby.backend.presentation.api.mapper.ExperimentPostMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Tag(name = "실험 공고 API - /v1/experiment-posts")
@RestController
@RequestMapping("/v1/experiment-posts")
class ExperimentPostController (
    private val experimentPostService: ExperimentPostService,
    private val paginationService: PaginationService
){
    @PreAuthorize("hasRole('RESEARCHER')")
    @PostMapping
    @Operation(
        summary = "공고 등록 API- 연구자 공고 등록",
        description = "연구자가 실험자를 모집하는 공고를 등록합니다."
    )
    fun createExperimentPost(
        @RequestBody @Valid request: CreateExperimentPostRequest
    ): CreateExperimentPostResponse {
        val input = ExperimentPostMapper.toCreatePostUseCaseInput(request)
        val output = experimentPostService.createNewExperimentPost(input)
        return ExperimentPostMapper.toCreateExperimentPostResponse(output)
    }

    @PreAuthorize("hasRole('RESEARCHER')")
    @PatchMapping("/{postId}/recruit-status")
    @Operation(
        summary = "연구자가 작성한 특정 실험 공고 모집 상태 수정",
        description = "로그인한 연구자가 작성한 특정 실험 공고의 모집 상태를 변경합니다"
    )
    fun updateExperimentPostRecruitStatus(
        @PathVariable postId: Long
    ): MyExperimentPostResponse {
        val input = ExperimentPostMapper.toUpdateExperimentPostRecruitStatusUseCaseInput(postId)
        val output = experimentPostService.updateExperimentPostRecruitStatus(input)
        return ExperimentPostMapper.toExperimentPostResponse(output)
    }

    @PreAuthorize("hasRole('RESEARCHER')")
    @PostMapping("/image-upload-request")
    @Operation(
        summary = "공고 사진 S3 Presigned Url 요청",
        description = "S3 Presigned Url을 요청합니다."
    )
    fun requestPreSignedUrl(
        @RequestBody @Valid request: PreSignedUrlRequest
    ): PreSignedUrlResponse {
        val input = ExperimentPostMapper.toGeneratePreSignedUrlUseCaseInput(request)
        val output = experimentPostService.generatePreSignedUrl(input)
        return ExperimentPostMapper.toGeneratePreSignedUrlResponse(output)
    }

    @PostMapping("/{postId}/details")
    @Operation(
        summary = "특정 공고 상세 정보 조회 API",
        description = "특정 공고 상세 정보를 반환합니다"
    )
    fun getExperimentPostDetail(
        @PathVariable postId: Long
    ): ExperimentPostDetailResponse {
        val input = ExperimentPostMapper.toGetExperimentPostDetailUseCaseInput(postId)
        val output = experimentPostService.getExperimentPostDetail(input)
        return ExperimentPostMapper.toGetExperimentPostDetailResponse(output)
    }

    @GetMapping("/counts")
    @Operation(
        summary = "등록된 공고 수 조회 API",
        description = "지역 별로 등록된 공고 수를 조회합니다"
    )
    fun getExperimentPostCounts(
        @RequestParam(required = false) region: String?,
        @RequestParam(required = false, defaultValue = "ALL") recruitStatus: RecruitStatus
    ): ExperimentPostCountsResponse {
        val input = ExperimentPostMapper.toGetExperimentPostCountsUseCaseInput(region, recruitStatus)
        val output = experimentPostService.getExperimentPostCounts(input)
        return ExperimentPostMapper.toGetExperimentPostCountsResponse(output)
    }

    @GetMapping("/{postId}/apply-method")
    @Operation(
        summary = "특정 공고 지원 방법 조회 API",
        description = "특정 공고의 지원 방법을 반환합니다."
    )
    fun getExperimentPostApplyMethod(
        @PathVariable postId: Long
    ): ExperimentPostApplyMethodResponse {
        val input = ExperimentPostMapper.toGetExperimentPostApplyMethodUseCaseInput(postId)
        val output = experimentPostService.getExperimentPostApplyMethod(input)
        return ExperimentPostMapper.toGetExperimentPostApplyMethodResponse(output)
    }

    @GetMapping("/search")
    @Operation(
        summary = "공고 전체 조회 API - 필터링 + 페이지네이션 적용" ,
        description = "사용자가 필터링한 조건에 맞는 공고 목록들을 조회합니다"
    )
    fun getExperimentPosts(
        @RequestParam(required = false) matchType: MatchType?,
        @RequestParam(required = false) gender: GenderType?,
        @RequestParam(required = false) age: Int?,
        @RequestParam(required = false) region: Region?,
        @RequestParam(required = false) areas: List<Area>?,
        @RequestParam(required = false, defaultValue = "ALL") recruitStatus: RecruitStatus,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "6") count: Int
    ): PaginatedResponse<ExperimentPostResponse> {
        val customFilter = ExperimentPostMapper.toUseCaseCustomFilter(matchType, gender, age, region, areas, recruitStatus)
        val pagination = ExperimentPostMapper.toGetExperimentPostsUseCasePagination(page, count)
        val input = ExperimentPostMapper.toGetExperimentPostsUseCaseInput(customFilter, pagination)
        val posts = experimentPostService.getExperimentPosts(input)

        val totalCountInput = ExperimentPostMapper.toGetExperimentPostTotalCountUseCaseInput(customFilter)
        val totalCount = experimentPostService.getExperimentPostTotalCount(totalCountInput)
        val isLast = paginationService.isLastPage(totalCount, page, count)
        return ExperimentPostMapper.toGetExperimentPostsResponse(posts, page, totalCount, isLast)
    }

    @PreAuthorize("hasRole('RESEARCHER')")
    @GetMapping("/my-posts")
    @Operation(
        summary = "연구자가 작성한 실험 공고 리스트 조회",
        description = "로그인한 연구자가 작성한 실험 공고 리스트를 반환합니다"
    )
    fun getMyExperimentPosts(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "6") count: Int,
        @RequestParam(defaultValue = "DESC") order: String
    ): PaginatedResponse<MyExperimentPostResponse> {
        val pagination = ExperimentPostMapper.toGetMyExperimentPostsUseCasePagination(page, count, order)
        val input = ExperimentPostMapper.toGetMyExperimentPosts(pagination)
        val posts = experimentPostService.getMyExperimentPosts(input)

        val totalCountInput = ExperimentPostMapper.toGetTotalMyExperimentPostCountUseCaseInput()
        val totalCount = experimentPostService.getMyExperimentPostsCount(totalCountInput).totalPostCount
        val isLast = paginationService.isLastPage(totalCount, count, page)
        return ExperimentPostMapper.toGetMyExperimentPostsResponse(posts, page, totalCount, isLast)
    }
}
