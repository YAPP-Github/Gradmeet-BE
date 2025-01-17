package com.dobby.backend.presentation.api.mapper

import com.dobby.backend.application.usecase.experiment.*
import com.dobby.backend.application.usecase.experiment.CreateExperimentPostUseCase
import com.dobby.backend.application.usecase.experiment.GetExperimentPostApplyMethodUseCase
import com.dobby.backend.application.usecase.experiment.GetExperimentPostDetailUseCase
import com.dobby.backend.presentation.api.dto.request.PreSignedUrlRequest
import com.dobby.backend.presentation.api.dto.response.PreSignedUrlResponse
import com.dobby.backend.infrastructure.database.entity.enums.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import com.dobby.backend.presentation.api.dto.request.experiment.*
import com.dobby.backend.presentation.api.dto.response.experiment.*
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
            timeRequired = request.timeRequired,
            reward = request.reward,
            startDate = request.startDate,
            endDate = request.endDate,
            matchType = request.matchType,
            detailedAddress = request.detailedAddress,
            leadResearcher = request.leadResearcher,
        )
    }


    private fun toApplyMethodInfo(dto: ApplyMethodInfo): CreateExperimentPostUseCase.ApplyMethodInfo {
        return CreateExperimentPostUseCase.ApplyMethodInfo(
            content = dto.content,
            formUrl = dto.formUrl,
            phoneNum = dto.phoneNum
        )
    }

    private fun toTargetGroupInfo(dto: TargetGroupInfo): CreateExperimentPostUseCase.TargetGroupInfo {
        return CreateExperimentPostUseCase.TargetGroupInfo(
            startAge = dto.startAge,
            endAge = dto.endAge,
            genderType = dto.genderType,
            otherCondition = dto.otherCondition
        )
    }


    private fun toImageListInfo(dto: ImageListInfo): CreateExperimentPostUseCase.ImageListInfo {
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
            experimentPostId = input.postId,
            title = input.title,
            views = input.views,
            durationInfo = DurationInfo(
                startDate = input.durationInfo?.startDate,
                endDate = input.durationInfo?.endDate
            ),
            reward = input.reward,
            univName = input.univName
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
            recruitDone = response.experimentPostDetailResponse.recruitDone,
            summary = response.experimentPostDetailResponse.summary,
            targetGroup = response.experimentPostDetailResponse.targetGroup,
            address = response.experimentPostDetailResponse.address,
            content = response.experimentPostDetailResponse.content,
            imageList = response.experimentPostDetailResponse.imageList
        )
    }

    fun toGetExperimentPostCountsUseCaseInput(region: String?): Any {
        return if (region == null) {
            GetExperimentPostCountsByRegionUseCase.Input(region = null)
        } else {
            GetExperimentPostCountsByAreaUseCase.Input(region = region)
        }
    }

    fun toGetExperimentPostCountsResponse(output: Any): ExperimentPostCountsResponse {
        return when (output) {
            is GetExperimentPostCountsByAreaUseCase.Output -> {
                ExperimentPostCountsResponse(
                    total = output.total,
                    data = output.area.map {
                        DataCount(
                            name = it.name,
                            count = it.count
                        )
                    }
                )
            }
            is GetExperimentPostCountsByRegionUseCase.Output -> {
                ExperimentPostCountsResponse(
                    total = output.total,
                    data = output.area.map {
                        DataCount(
                            name = it.name,
                            count = it.count
                        )
                    }
                )
            }
            else -> throw IllegalArgumentException("Unsupported output type: ${output::class.simpleName}")
        }
    }

    fun toGetExperimentPostApplyMethodUseCaseInput(experimentPostId: Long): GetExperimentPostApplyMethodUseCase.Input {
        return GetExperimentPostApplyMethodUseCase.Input(
            experimentPostId = experimentPostId
        )
    }

    fun toGetExperimentPostApplyMethodResponse(output: GetExperimentPostApplyMethodUseCase.Output): ExperimentPostApplyMethodResponse {
        return ExperimentPostApplyMethodResponse(
            applyMethodId = output.applyMethodId,
            phoneNum = output.phoneNum,
            formUrl = output.formUrl,
            content = output.content
        )
    }

    fun toUseCaseCustomFilter(
        matchType: MatchType?,
        gender: GenderType?,
        age: Int?,
        region: Region?,
        areas: List<Area>?,
        recruitDone: Boolean?
    ): GetExperimentPostsUseCase.CustomFilterInput {
        return GetExperimentPostsUseCase.CustomFilterInput(
            matchType = matchType,
            studyTarget = GetExperimentPostsUseCase.StudyTargetInput(
                gender = gender,
                age = age
            ),
            locationTarget = GetExperimentPostsUseCase.LocationTargetInput(
                region = region,
                areas = areas
            ),
            recruitDone = recruitDone
        )
    }

    fun toUseCasePagination(
        page: Int, count: Int
    ) : GetExperimentPostsUseCase.PaginationInput {
        return GetExperimentPostsUseCase.PaginationInput(
            page = page,
            count = count,
        )
    }

    fun toGetExperimentPostsUseCaseInput(
        customFilter: GetExperimentPostsUseCase.CustomFilterInput,
        pagination: GetExperimentPostsUseCase.PaginationInput
    ): GetExperimentPostsUseCase.Input {
        return GetExperimentPostsUseCase.Input(
            customFilter = customFilter,
            pagination = pagination
        )
    }

    fun toGetExperimentPostsResponse(output: GetExperimentPostsUseCase.Output): ExperimentPostsResponse {
        return ExperimentPostsResponse(
            postInfo = PostInfo(
                experimentPostId = output.postInfo.experimentPostId,
                title = output.postInfo.title,
                views = output.postInfo.views,
                univName = output.postInfo.univName,
                reward = output.postInfo.reward,
                durationInfo = DurationInfo(
                    startDate = output.postInfo.durationInfo?.startDate,
                    endDate = output.postInfo.durationInfo?.endDate

                )
            ),
            recuritDone = output.postInfo.recruitDone
        )
    }

    fun toGeneratePreSignedUrlUseCaseInput(request: PreSignedUrlRequest): GenerateExperimentPostPreSignedUrlUseCase.Input {
        return GenerateExperimentPostPreSignedUrlUseCase.Input(
            fileName = request.fileName
        )
    }

    fun toGeneratePreSignedUrlResponse(output: GenerateExperimentPostPreSignedUrlUseCase.Output): PreSignedUrlResponse {
        return PreSignedUrlResponse(
            preSignedUrl = output.preSignedUrl
        )
    }
}
