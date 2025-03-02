package com.dobby.backend.presentation.api.mapper

import com.dobby.backend.application.usecase.experiment.*
import com.dobby.backend.application.usecase.experiment.CreateExperimentPostUseCase
import com.dobby.backend.application.usecase.experiment.GetExperimentPostApplyMethodUseCase
import com.dobby.backend.application.usecase.experiment.GetExperimentPostDetailUseCase
import com.dobby.backend.presentation.api.dto.request.PreSignedUrlRequest
import com.dobby.backend.presentation.api.dto.response.PreSignedUrlResponse
import com.dobby.enums.member.GenderType
import com.dobby.enums.MatchType
import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import com.dobby.enums.experiment.RecruitStatus
import com.dobby.backend.presentation.api.dto.request.experiment.*
import com.dobby.backend.presentation.api.dto.response.PaginatedResponse
import com.dobby.backend.presentation.api.dto.response.experiment.*
import com.dobby.backend.presentation.api.dto.response.member.MyExperimentPostResponse
import com.dobby.backend.util.getCurrentMemberId
import com.dobby.backend.util.getCurrentMemberIdOrNull

object ExperimentPostMapper {
    fun toCreatePostUseCaseInput(request: CreateExperimentPostRequest): CreateExperimentPostUseCase.Input {
        return CreateExperimentPostUseCase.Input(
            memberId = getCurrentMemberId(),
            applyMethodInfo = toCreateApplyMethodInfo(request.applyMethodInfo),
            targetGroupInfo = toCreateTargetGroupInfo(request.targetGroupInfo),
            imageListInfo = toCreateImageListInfo(request.imageListInfo),
            title = request.title,
            content = request.content,
            alarmAgree = request.alarmAgree,
            place = request.place,
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


    private fun toCreateApplyMethodInfo(dto: ApplyMethodInfo): CreateExperimentPostUseCase.ApplyMethodInfo {
        return CreateExperimentPostUseCase.ApplyMethodInfo(
            content = dto.content,
            formUrl = dto.formUrl,
            phoneNum = dto.phoneNum
        )
    }

    private fun toCreateTargetGroupInfo(dto: TargetGroupInfo): CreateExperimentPostUseCase.TargetGroupInfo {
        return CreateExperimentPostUseCase.TargetGroupInfo(
            startAge = dto.startAge,
            endAge = dto.endAge,
            genderType = dto.genderType,
            otherCondition = dto.otherCondition
        )
    }

    private fun toCreateImageListInfo(dto: ImageListInfo): CreateExperimentPostUseCase.ImageListInfo {
        return CreateExperimentPostUseCase.ImageListInfo(
            images = dto.images?: emptyList()
        )
    }

    fun toCreateExperimentPostResponse(response: CreateExperimentPostUseCase.Output): CreateExperimentPostResponse{
        return CreateExperimentPostResponse(
            postInfo = toCreatePostInfo(response.postInfo)
        )
    }

    private fun toCreatePostInfo(input: CreateExperimentPostUseCase.PostInfo): PostInfo{
        return PostInfo(
            experimentPostId = input.postId,
            title = input.title,
            views = input.views,
            durationInfo = DurationInfo(
                startDate = input.durationInfo?.startDate,
                endDate = input.durationInfo?.endDate
            ),
            reward = input.reward,
            place = input.place
        )
    }


    private fun toUpdateApplyMethodInfo(dto: ApplyMethodInfo): UpdateExperimentPostUseCase.ApplyMethodInfo {
        return UpdateExperimentPostUseCase.ApplyMethodInfo(
            content = dto.content,
            formUrl = dto.formUrl,
            phoneNum = dto.phoneNum
        )
    }

    private fun toUpdateTargetGroupInfo(dto: TargetGroupInfo): UpdateExperimentPostUseCase.TargetGroupInfo {
        return UpdateExperimentPostUseCase.TargetGroupInfo(
            startAge = dto.startAge,
            endAge = dto.endAge,
            genderType = dto.genderType,
            otherCondition = dto.otherCondition
        )
    }

    private fun toUpdateImageListInfo(dto: ImageListInfo): UpdateExperimentPostUseCase.ImageListInfo {
        return UpdateExperimentPostUseCase.ImageListInfo(
            images = dto.images?: emptyList()
        )
    }


    private fun toUpdatePostInfo(input: UpdateExperimentPostUseCase.PostInfo): PostInfo{
        return PostInfo(
            experimentPostId = input.postId,
            title = input.title,
            views = input.views,
            durationInfo = DurationInfo(
                startDate = input.durationInfo?.startDate,
                endDate = input.durationInfo?.endDate
            ),
            reward = input.reward,
            place = input.place
        )
    }

    fun toUpdateExperimentPostInput(request: UpdateExperimentPostRequest, postId: String): UpdateExperimentPostUseCase.Input {
        return UpdateExperimentPostUseCase.Input(
            experimentPostId = postId,
            memberId = getCurrentMemberId(),
            applyMethodInfo = toUpdateApplyMethodInfo(request.applyMethodInfo),
            targetGroupInfo = toUpdateTargetGroupInfo(request.targetGroupInfo),
            imageListInfo = toUpdateImageListInfo(request.imageListInfo),
            title = request.title,
            content = request.content,
            place = request.place,
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
            recruitStatus = request.recruitStatus
        )
    }

    fun toUpdateExperimentPostResponse(response: UpdateExperimentPostUseCase.Output): UpdateExperimentPostResponse {
        return UpdateExperimentPostResponse(
            postInfo = toUpdatePostInfo(response.postInfo)
        )
    }

    fun toGetExperimentPostDetailForUpdateUseCaseInput(experimentPostId: String): GetExperimentPostDetailForUpdateUseCase.Input {
        return GetExperimentPostDetailForUpdateUseCase.Input(
            experimentPostId = experimentPostId,
            memberId = getCurrentMemberId()
        )
    }

    fun toGetExperimentPostDetailForUpdateResponse(response: GetExperimentPostDetailForUpdateUseCase.Output): ExperimentPostDetailResponse {
        return ExperimentPostDetailResponse(
            experimentPostId = response.experimentPostDetail.experimentPostId,
            title = response.experimentPostDetail.title,
            uploadDate = response.experimentPostDetail.uploadDate,
            uploaderName = response.experimentPostDetail.uploaderName,
            views = response.experimentPostDetail.views,
            recruitStatus = response.experimentPostDetail.recruitStatus,
            summary = response.experimentPostDetail.summary.toResponse(),
            targetGroup = response.experimentPostDetail.targetGroup.toResponse(),
            address = response.experimentPostDetail.address.toResponse(),
            content = response.experimentPostDetail.content,
            imageList = response.experimentPostDetail.imageList,
            isAuthor = response.experimentPostDetail.isAuthor,
            isUploaderActive = response.experimentPostDetail.isUploaderActive,
            alarmAgree = response.experimentPostDetail.alarmAgree
        )
    }

    private fun GetExperimentPostDetailForUpdateUseCase.ExperimentPostDetailForUpdate.Summary.toResponse(): ExperimentPostDetailResponse.SummaryResponse {
        return ExperimentPostDetailResponse.SummaryResponse(
            startDate = this.startDate,
            endDate = this.endDate,
            leadResearcher = this.leadResearcher,
            matchType = this.matchType,
            reward = this.reward,
            count = this.count,
            timeRequired = this.timeRequired
        )
    }

    private fun GetExperimentPostDetailForUpdateUseCase.ExperimentPostDetailForUpdate.TargetGroup.toResponse(): ExperimentPostDetailResponse.TargetGroupResponse {
        return ExperimentPostDetailResponse.TargetGroupResponse(
            startAge = this.startAge,
            endAge = this.endAge,
            genderType = this.genderType,
            otherCondition = this.otherCondition
        )
    }

    private fun GetExperimentPostDetailForUpdateUseCase.ExperimentPostDetailForUpdate.Address.toResponse(): ExperimentPostDetailResponse.AddressResponse {
        return ExperimentPostDetailResponse.AddressResponse(
            place = this.place,
            region = this.region,
            area = this.area,
            detailedAddress = this.detailedAddress
        )
    }

    fun toGetExperimentPostDetailUseCaseInput(experimentPostId: String): GetExperimentPostDetailUseCase.Input {
        return GetExperimentPostDetailUseCase.Input(
            experimentPostId = experimentPostId,
            memberId = getCurrentMemberIdOrNull()
        )
    }

    fun toGetExperimentPostDetailResponse(response: GetExperimentPostDetailUseCase.Output): ExperimentPostDetailResponse {
        return ExperimentPostDetailResponse(
            experimentPostId = response.experimentPostDetail.experimentPostId,
            title = response.experimentPostDetail.title,
            uploadDate = response.experimentPostDetail.uploadDate,
            uploaderName = response.experimentPostDetail.uploaderName,
            views = response.experimentPostDetail.views,
            recruitStatus = response.experimentPostDetail.recruitStatus,
            summary = response.experimentPostDetail.summary.toResponse(),
            targetGroup = response.experimentPostDetail.targetGroup.toResponse(),
            address = response.experimentPostDetail.address.toResponse(),
            content = response.experimentPostDetail.content,
            imageList = response.experimentPostDetail.imageList,
            isAuthor = response.experimentPostDetail.isAuthor,
            isUploaderActive = response.experimentPostDetail.isUploaderActive,
            alarmAgree = response.experimentPostDetail.alarmAgree
        )
    }

    private fun GetExperimentPostDetailUseCase.ExperimentPostDetail.Summary.toResponse(): ExperimentPostDetailResponse.SummaryResponse {
        return ExperimentPostDetailResponse.SummaryResponse(
            startDate = this.startDate,
            endDate = this.endDate,
            leadResearcher = this.leadResearcher,
            matchType = this.matchType,
            reward = this.reward,
            count = this.count,
            timeRequired = this.timeRequired
        )
    }

    private fun GetExperimentPostDetailUseCase.ExperimentPostDetail.TargetGroup.toResponse(): ExperimentPostDetailResponse.TargetGroupResponse {
        return ExperimentPostDetailResponse.TargetGroupResponse(
            startAge = this.startAge,
            endAge = this.endAge,
            genderType = this.genderType,
            otherCondition = this.otherCondition
        )
    }

    private fun GetExperimentPostDetailUseCase.ExperimentPostDetail.Address.toResponse(): ExperimentPostDetailResponse.AddressResponse {
        return ExperimentPostDetailResponse.AddressResponse(
            place = this.place,
            region = this.region,
            area = this.area,
            detailedAddress = this.detailedAddress
        )
    }

    fun toGetExperimentPostCountsUseCaseInput(region: String?, recruitStatus: RecruitStatus): Any {
        return if (region == null) {
            GetExperimentPostCountsByRegionUseCase.Input(region = null, recruitStatus)
        } else {
            GetExperimentPostCountsByAreaUseCase.Input(region = region, recruitStatus)
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

    fun toGetExperimentPostApplyMethodUseCaseInput(experimentPostId: String): GetExperimentPostApplyMethodUseCase.Input {
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
        recruitStatus: RecruitStatus
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
            recruitStatus = recruitStatus
        )
    }

    fun toGetExperimentPostsUseCasePagination(
        page: Int,
        count: Int,
        order: String
    ) : GetExperimentPostsUseCase.PaginationInput {
        return GetExperimentPostsUseCase.PaginationInput(
            page = page,
            count = count,
            order = order
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


    fun toGetExperimentPostsResponse(
        output: List<GetExperimentPostsUseCase.Output>,
        page: Int,
        totalCount: Int,
        isLast: Boolean
    ): PaginatedResponse<ExperimentPostResponse> {
        return PaginatedResponse(
            content = output.map { post ->
                ExperimentPostResponse(
                    postInfo = PostInfo(
                        experimentPostId = post.postInfo.experimentPostId,
                        title = post.postInfo.title,
                        views = post.postInfo.views,
                        place = post.postInfo.place,
                        reward = post.postInfo.reward,
                        durationInfo = DurationInfo(
                            startDate = post.postInfo.durationInfo.startDate,
                            endDate = post.postInfo.durationInfo.endDate
                        )
                    ),
                    recruitStatus = post.postInfo.recruitStatus
                )
            },
            page = page,
            size = output.size,
            totalCount = totalCount,
            isLast = isLast
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

    fun toGetExperimentPostTotalCountUseCaseInput(customFilter: GetExperimentPostsUseCase.CustomFilterInput): GetExperimentPostTotalCountByCustomFilterUseCase.Input {
        return GetExperimentPostTotalCountByCustomFilterUseCase.Input(
            matchType = customFilter.matchType,
            studyTarget = customFilter.studyTarget?.let {
                GetExperimentPostsUseCase.StudyTargetInput(
                    gender = it.gender,
                    age = it.age
                )
            },
            locationTarget = customFilter.locationTarget?.let {
                GetExperimentPostsUseCase.LocationTargetInput(
                    region = it.region,
                    areas = it.areas
                )
            },
            recruitStatus = customFilter.recruitStatus
        )
    }

    fun toDeleteExperimentPostUseCaseInput(postId: String): DeleteExperimentPostUseCase.Input {
        return DeleteExperimentPostUseCase.Input(
            memberId = getCurrentMemberId(),
            postId = postId
        )
    }

    fun toUpdateExperimentPostRecruitStatusUseCaseInput(postId: String): UpdateExperimentPostRecruitStatusUseCase.Input {
        return UpdateExperimentPostRecruitStatusUseCase.Input(
            memberId = getCurrentMemberId(),
            postId = postId
        )
    }

    fun toExperimentPostResponse(output: UpdateExperimentPostRecruitStatusUseCase.Output): MyExperimentPostResponse {
        return MyExperimentPostResponse(
            experimentPostId = output.experimentPost.id,
            title = output.experimentPost.title,
            content = output.experimentPost.content,
            views = output.experimentPost.views,
            recruitStatus = output.experimentPost.recruitStatus,
            uploadDate = output.experimentPost.createdAt.toLocalDate()
        )
    }

    fun toGetMyExperimentPostsResponse(
        output: List<GetMyExperimentPostsUseCase.Output>,
        page: Int,
        totalCount: Int,
        isLast: Boolean
    ): PaginatedResponse<MyExperimentPostResponse> {
        return PaginatedResponse(
            content = output.map { post ->
                MyExperimentPostResponse(
                    experimentPostId = post.experimentPostId,
                    title = post.title,
                    content = post.content,
                    views = post.views,
                    recruitStatus = post.recruitStatus,
                    uploadDate = post.uploadDate
                )
            },
            page = page,
            size = output.size,
            totalCount = totalCount,
            isLast = isLast
        )
    }

    fun toGetMyExperimentPosts(
        pagination: GetMyExperimentPostsUseCase.PaginationInput
    ): GetMyExperimentPostsUseCase.Input {
        return GetMyExperimentPostsUseCase.Input(
            memberId = getCurrentMemberId(),
            pagination = pagination
        )
    }


    fun toGetTotalMyExperimentPostCountUseCaseInput(): GetMyExperimentPostTotalCountUseCase.Input {
        return GetMyExperimentPostTotalCountUseCase.Input(
            memberId = getCurrentMemberId()
        )
    }

    fun toGetMyExperimentPostsUseCasePagination(
        page: Int, count: Int, order: String
    ) : GetMyExperimentPostsUseCase.PaginationInput {
        return GetMyExperimentPostsUseCase.PaginationInput(
            page = page,
            count = count,
            order = order
        )
    }
}
