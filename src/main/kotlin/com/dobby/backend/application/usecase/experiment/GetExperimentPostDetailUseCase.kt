package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.ExperimentPostNotFoundException
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.domain.model.experiment.TargetGroup
import com.dobby.backend.presentation.api.dto.response.experiment.ExperimentPostDetailResponse

class GetExperimentPostDetailUseCase(
    private val experimentPostGateway: ExperimentPostGateway
) : UseCase<GetExperimentPostDetailUseCase.Input, GetExperimentPostDetailUseCase.Output> {

    data class Input(
        val experimentPostId: Long,
        val memberId: Long?
    )

    data class Output(
        val experimentPostDetailResponse: ExperimentPostDetailResponse
    )

    override fun execute(input: Input): Output {
        val experimentPost = experimentPostGateway.findById(input.experimentPostId)
            ?: throw ExperimentPostNotFoundException()
        experimentPost.incrementViews()
        experimentPostGateway.save(experimentPost)

        return Output(
            experimentPostDetailResponse = experimentPost.toDetailResponse(input.memberId)
        )
    }
}

fun ExperimentPost.toDetailResponse(memberId: Long?): ExperimentPostDetailResponse {
    return ExperimentPostDetailResponse(
        experimentPostId = this.id,
        title = this.title,
        uploadDate = this.createdAt.toLocalDate(),
        uploaderName = this.member.name,
        views = this.views,
        recruitStatus = this.recruitStatus,
        summary = this.toSummaryResponse(),
        targetGroup = this.targetGroup.toResponse(),
        address = this.toAddressResponse(),
        content = this.content,
        imageList = this.images.map { it.imageUrl },
        isAuthor = this.member.id == memberId
    )
}

fun ExperimentPost.toSummaryResponse(): ExperimentPostDetailResponse.SummaryResponse {
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

fun TargetGroup.toResponse(): ExperimentPostDetailResponse.TargetGroupResponse {
    return ExperimentPostDetailResponse.TargetGroupResponse(
        startAge = this.startAge,
        endAge = this.endAge,
        genderType = this.genderType,
        otherCondition = this.otherCondition
    )
}

fun ExperimentPost.toAddressResponse(): ExperimentPostDetailResponse.AddressResponse {
    return ExperimentPostDetailResponse.AddressResponse(
        univName = this.univName,
        region = this.region,
        area = this.area,
        detailedAddress = this.detailedAddress
    )
}
