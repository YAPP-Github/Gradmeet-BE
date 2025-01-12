package com.dobby.backend.presentation.api.mapper

import com.dobby.backend.application.usecase.experiment.CreateExperimentPostUseCase
import com.dobby.backend.application.usecase.experiment.GetExperimentPostDetailUseCase
import com.dobby.backend.application.usecase.experiment.GetResearcherInfoUseCase
import com.dobby.backend.presentation.api.dto.request.expirement.CreateExperimentPostRequest
import com.dobby.backend.presentation.api.dto.response.expirement.CreateExperimentPostResponse
import com.dobby.backend.presentation.api.dto.response.expirement.DefaultInfoResponse
import com.dobby.backend.presentation.api.dto.response.expirement.ExperimentPostDetailResponse
import com.dobby.backend.presentation.api.dto.response.expirement.PostInfo
import com.dobby.backend.util.getCurrentMemberId

object ExperimentPostMapper {
    fun toCreatePostUseCaseInput(request: CreateExperimentPostRequest): CreateExperimentPostUseCase.Input {
        return CreateExperimentPostUseCase.Input(
            memberId = getCurrentMemberId(),
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
            leadResearcher = request.leadResearcher,
        )
    }

    private fun toApplyMethodInfo(dto: com.dobby.backend.presentation.api.dto.request.expirement.ApplyMethodInfo): CreateExperimentPostUseCase.ApplyMethodInfo {
        return CreateExperimentPostUseCase.ApplyMethodInfo(
            content = dto.content,
            formUrl = dto.formUrl,
            phoneNum = dto.phoneNum
        )
    }

    private fun toTargetGroupInfo(dto: com.dobby.backend.presentation.api.dto.request.expirement.TargetGroupInfo): CreateExperimentPostUseCase.TargetGroupInfo {
        return CreateExperimentPostUseCase.TargetGroupInfo(
            startAge = dto.startAge,
            endAge = dto.endAge,
            genderType = dto.genderType,
            otherCondition = dto.otherCondition
        )
    }

    private fun toImageListInfo(dto: com.dobby.backend.presentation.api.dto.request.expirement.ImageListInfo): CreateExperimentPostUseCase.ImageListInfo {
        return CreateExperimentPostUseCase.ImageListInfo(
            images = dto.images
        )
    }

    fun toCreateExperimentPostResponse(response: CreateExperimentPostUseCase.Output): CreateExperimentPostResponse{
        return CreateExperimentPostResponse(
            postInfo = toPostInfo(response.postInfo)
        )
    }

    private fun toPostInfo(input: CreateExperimentPostUseCase.PostInfo): PostInfo{
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

    fun toDefaultInfoUseCaseInput(): GetResearcherInfoUseCase.Input {
        return GetResearcherInfoUseCase.Input(
            memberId = getCurrentMemberId()
        )
    }

    fun toDefaultInfoResponse(response: GetResearcherInfoUseCase.Output): DefaultInfoResponse{
        return DefaultInfoResponse(
            leadResearcher = response.leadResearcher,
            univName = response.univName
        )
    }

    fun toGetExperimentPostDetailUseCaseInput(experimentPostId: Long): GetExperimentPostDetailUseCase.Input {
        return GetExperimentPostDetailUseCase.Input(
            experimentPostId = experimentPostId
        )
    }

    fun toGetExperimentPostDetailResponse(response: GetExperimentPostDetailUseCase.Output): ExperimentPostDetailResponse {
        return ExperimentPostDetailResponse(
            experimentPostId = response.experimentPostDetailResponse.experimentPostId,
            title = response.experimentPostDetailResponse.title,
            uploadDate = response.experimentPostDetailResponse.uploadDate,
            uploaderName = response.experimentPostDetailResponse.uploaderName,
            views = response.experimentPostDetailResponse.views,
            state = response.experimentPostDetailResponse.state,
            summary = response.experimentPostDetailResponse.summary,
            targetGroup = response.experimentPostDetailResponse.targetGroup,
            address = response.experimentPostDetailResponse.address,
            content = response.experimentPostDetailResponse.content,
            imageList = response.experimentPostDetailResponse.imageList
        )
    }
}
