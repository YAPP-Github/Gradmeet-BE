package com.dobby.backend.presentation.api.controller

import com.dobby.backend.application.service.ExperimentPostService
import com.dobby.backend.presentation.api.dto.request.expirement.CreateExperimentPostRequest
import com.dobby.backend.presentation.api.dto.response.expirement.CreateExperimentPostResponse
import com.dobby.backend.presentation.api.dto.response.expirement.DefaultInfoResponse
import com.dobby.backend.presentation.api.mapper.ExperimentPostMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@Tag(name = "[연구자] 공고 등록 API - /v1/experiment-posts")
@RestController
@RequestMapping("/v1/experiment-posts")
class ExperimentPostController (
    private val experimentPostService: ExperimentPostService
){
    @PostMapping("")
    @Operation(
        summary = "공고 등록 API- 연구자 공고 등록",
        description = "연구자가 실험자를 모집하는 공고를 등록합니다."
    )
    fun createPost(
        @RequestBody @Valid request: CreateExperimentPostRequest
    ): CreateExperimentPostResponse {
        val input = ExperimentPostMapper.toCreatePostUseCaseInput(request)
        val output = experimentPostService.createNewExperimentPost(input)
        return ExperimentPostMapper.toCreateExperimentPostResponse(output)
    }

    @GetMapping("/default")
    @Operation(
        summary = "공고 등록 API- 연구자 기본 정보 렌더링",
        description = "연구자의 기본 정보 [학교 + 전공 + 랩실 정보 + 이름]를 반환합니다."
    )
    fun getDefaultInfo(): DefaultInfoResponse {
        val input = ExperimentPostMapper.toDefaultInfoUseCaseInput()
        val output = experimentPostService.getDefaultInfo(input)
        return ExperimentPostMapper.toDefaultInfoResponse(output)
    }
}
