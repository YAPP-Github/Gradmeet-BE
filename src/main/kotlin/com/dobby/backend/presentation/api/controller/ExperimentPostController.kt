package com.dobby.backend.presentation.api.controller

import com.dobby.backend.application.service.ExperimentPostService
import com.dobby.backend.presentation.api.dto.request.PreSignedUrlRequest
import com.dobby.backend.presentation.api.dto.request.expirement.CreateExperimentPostRequest
import com.dobby.backend.presentation.api.dto.response.PreSignedUrlResponse
import com.dobby.backend.presentation.api.dto.response.expirement.CreateExperimentPostResponse
import com.dobby.backend.presentation.api.dto.response.expirement.ExperimentPostApplyMethodResponse
import com.dobby.backend.presentation.api.dto.response.expirement.ExperimentPostCountsResponse
import com.dobby.backend.presentation.api.dto.response.expirement.ExperimentPostDetailResponse
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
    private val experimentPostService: ExperimentPostService
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
        @RequestParam(required = false) region: String?
    ): ExperimentPostCountsResponse {
        val input = ExperimentPostMapper.toGetExperimentPostCountsUseCaseInput(region)
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
}
