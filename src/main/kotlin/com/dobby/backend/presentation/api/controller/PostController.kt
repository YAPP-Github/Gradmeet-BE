package com.dobby.backend.presentation.api.controller

import com.dobby.backend.application.service.PostService
import com.dobby.backend.presentation.api.dto.payload.ApiResponse
import com.dobby.backend.presentation.api.dto.request.expirement.CreatePostRequest
import com.dobby.backend.presentation.api.dto.response.expirement.CreatePostResponse
import com.dobby.backend.presentation.api.mapper.PostMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "[실험자] 공고 등록 API - /v1/experiment-posts")
@RestController
@RequestMapping("/v1/expirement-posts")
class PostController (
    private val postService: PostService
){
    @PostMapping("")
    @Operation(
        summary = "공고 등록 API- 연구자 공고 등록",
        description = "연구자가 실험자를 모집하는 공고를 등록합니다."
    )
    fun createPost(
        @RequestBody @Valid request: CreatePostRequest
    ): ApiResponse<CreatePostResponse>{
        val input = PostMapper.toCreatePostUseCaseInput(request)
        val output = postService.createNewExperimentPost(input)
        val response = PostMapper.toCreatePostResponse(output)
        return ApiResponse.onSuccess(response)
    }
}
