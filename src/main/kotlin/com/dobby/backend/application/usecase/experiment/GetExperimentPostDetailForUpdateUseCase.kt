package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.exception.ExperimentPostNotFoundException
import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.model.experiment.ExperimentPost
import com.dobby.model.experiment.TargetGroup
import com.dobby.enums.MatchType
import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import com.dobby.enums.experiment.TimeSlot
import com.dobby.enums.member.GenderType
import java.time.LocalDate

class GetExperimentPostDetailForUpdateUseCase(
    private val experimentPostGateway: ExperimentPostGateway
) : UseCase<GetExperimentPostDetailForUpdateUseCase.Input, GetExperimentPostDetailForUpdateUseCase.Output> {

    data class Input(
        val experimentPostId: String,
        val memberId: String
    )

    data class Output(
        val experimentPostDetail: ExperimentPostDetailForUpdate
    )

    data class ExperimentPostDetailForUpdate(
        val experimentPostId: String,
        val title: String,
        val uploadDate: LocalDate,
        val uploaderName: String,
        val views: Int,
        val recruitStatus: Boolean,
        val summary: Summary,
        val targetGroup: TargetGroup,
        val address: Address,
        val content: String,
        val imageList: List<String>,
        val isAuthor: Boolean,
        val isUploaderActive: Boolean,
        val alarmAgree: Boolean
    ) {
        data class Summary(
            val startDate: LocalDate?,
            val endDate: LocalDate?,
            val leadResearcher: String,
            val matchType: MatchType,
            val reward: String,
            val count: Int?,
            val timeRequired: TimeSlot?
        )

        data class TargetGroup(
            val startAge: Int?,
            val endAge: Int?,
            val genderType: GenderType,
            val otherCondition: String?
        )

        data class Address(
            val place: String?,
            val region: Region?,
            val area: Area?,
            val detailedAddress: String?
        )
    }

    override fun execute(input: Input): Output {
        val post = experimentPostGateway.findExperimentPostByMemberIdAndPostId(
            memberId = input.memberId,
            postId = input.experimentPostId
        ) ?: throw ExperimentPostNotFoundException

        return Output(
            experimentPostDetail = post.toExperimentPostDetailForUpdate(input.memberId)
        )
    }
}

fun ExperimentPost.toExperimentPostDetailForUpdate(memberId: String): GetExperimentPostDetailForUpdateUseCase.ExperimentPostDetailForUpdate {
    return GetExperimentPostDetailForUpdateUseCase.ExperimentPostDetailForUpdate(
        experimentPostId = this.id,
        title = this.title,
        uploadDate = this.createdAt.toLocalDate(),
        uploaderName = this.member.name,
        views = this.views,
        recruitStatus = this.recruitStatus,
        summary = this.toSummaryForUpdate(),
        targetGroup = this.targetGroup.toTargetGroupForUpdate(),
        address = this.toAddressForUpdate(),
        content = this.content,
        imageList = this.images.map { it.imageUrl },
        isAuthor = this.member.id == memberId,
        isUploaderActive = this.member.deletedAt == null,
        alarmAgree = this.alarmAgree
    )
}

fun ExperimentPost.toSummaryForUpdate(): GetExperimentPostDetailForUpdateUseCase.ExperimentPostDetailForUpdate.Summary {
    return GetExperimentPostDetailForUpdateUseCase.ExperimentPostDetailForUpdate.Summary(
        startDate = this.startDate,
        endDate = this.endDate,
        leadResearcher = this.leadResearcher,
        matchType = this.matchType,
        reward = this.reward,
        count = this.count,
        timeRequired = this.timeRequired
    )
}

fun TargetGroup.toTargetGroupForUpdate(): GetExperimentPostDetailForUpdateUseCase.ExperimentPostDetailForUpdate.TargetGroup {
    return GetExperimentPostDetailForUpdateUseCase.ExperimentPostDetailForUpdate.TargetGroup(
        startAge = this.startAge,
        endAge = this.endAge,
        genderType = this.genderType,
        otherCondition = this.otherCondition
    )
}

fun ExperimentPost.toAddressForUpdate(): GetExperimentPostDetailForUpdateUseCase.ExperimentPostDetailForUpdate.Address {
    return GetExperimentPostDetailForUpdateUseCase.ExperimentPostDetailForUpdate.Address(
        place = this.place,
        region = this.region,
        area = this.area,
        detailedAddress = this.detailedAddress
    )
}
