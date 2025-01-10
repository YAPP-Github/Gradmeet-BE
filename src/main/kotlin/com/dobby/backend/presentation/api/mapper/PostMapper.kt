package com.dobby.backend.presentation.api.mapper

import com.dobby.backend.application.usecase.expirementUseCase.CreatePostUseCase
import com.dobby.backend.application.usecase.signupUseCase.CreateResearcherUseCase
import com.dobby.backend.presentation.api.dto.request.expirement.CreatePostRequest
import com.dobby.backend.presentation.api.dto.response.expirement.CreatePostResponse
import com.dobby.backend.presentation.api.dto.response.expirement.PostInfo

object PostMapper {
    fun toCreatePostUseCaseInput(request: CreatePostRequest): CreatePostUseCase.Input {
        return CreatePostUseCase.Input(
            applyMethodInfo = toApplyMethodInfo(request.applyMethodInfo),
            targetGroupInfo = toTargetGroupInfo(request.targetGroupInfo),
            imageListInfo = toImageListInfo(request.imageListInfo),
            title = request.title,
            content = request.content,
            alarmAgree = request.alarmAgree,
            univName = request.univName,
            count = request.count,
            region = request.region,
            area = request.area,
            durationMinutes = request.durationMinutes,
            reward = request.reward,
            startDate = request.startDate,
            endDate = request.endDate,
            matchType = request.matchType,
            detailedAddress = request.detailedAddress,
        )
    }

    private fun toApplyMethodInfo(dto: com.dobby.backend.presentation.api.dto.request.expirement.ApplyMethodInfo): CreatePostUseCase.ApplyMethodInfo {
        return CreatePostUseCase.ApplyMethodInfo(
            content = dto.content,
            formUrl = dto.formUrl,
            phoneNum = dto.phoneNum
        )
    }

    private fun toTargetGroupInfo(dto: com.dobby.backend.presentation.api.dto.request.expirement.TargetGroupInfo): CreatePostUseCase.TargetGroupInfo {
        return CreatePostUseCase.TargetGroupInfo(
            startAge = dto.startAge,
            endAge = dto.endAge,
            genderType = dto.genderType,
            otherCondition = dto.otherCondition
        )
    }

    private fun toImageListInfo(dto: com.dobby.backend.presentation.api.dto.request.expirement.ImageListInfo): CreatePostUseCase.ImageListInfo {
        return CreatePostUseCase.ImageListInfo(
            images = dto.images
        )
    }

    fun toCreatePostResponse(response: CreatePostUseCase.Output): CreatePostResponse{
        return CreatePostResponse(
            postInfo = toPostInfo(response.postInfo)
        )
    }

    private fun toPostInfo(input: CreatePostUseCase.PostInfo): PostInfo{
        return PostInfo(
            postId = input.postId,
            title = input.title,
            views = input.views,
            startDate = input.startDate,
            endDate = input.endDate,
            reward = input.reward,
            school = input.school
        )
    }
}
