package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.aop.DistributedLock
import com.dobby.backend.application.common.LockKeyProvider
import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.ExperimentPostNotFoundException
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.domain.model.experiment.TargetGroup
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import com.dobby.backend.infrastructure.database.entity.enums.experiment.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enums.member.GenderType
import java.time.LocalDate

open class GetExperimentPostDetailUseCase(
    private val experimentPostGateway: ExperimentPostGateway,
) : UseCase<GetExperimentPostDetailUseCase.Input, GetExperimentPostDetailUseCase.Output> {

    data class Input(
        val experimentPostId: String,
        val memberId: String?
    ) : LockKeyProvider {
        override fun getLockKey(): String {
            return "experimentPost:$experimentPostId"
        }
    }

    data class Output(
        val experimentPostDetail: ExperimentPostDetail
    )

    data class ExperimentPostDetail(
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

    @DistributedLock(keyPrefix = "lock:experimentPost")
    open override fun execute(input: Input): Output {
        val experimentPost = experimentPostGateway.findById(input.experimentPostId)
            ?: throw ExperimentPostNotFoundException
        experimentPost.incrementViews()
        experimentPostGateway.save(experimentPost)

        return Output(
            experimentPostDetail = experimentPost.toExperimentPostDetail(input.memberId)
        )
    }
}

fun ExperimentPost.toExperimentPostDetail(memberId: String?): GetExperimentPostDetailUseCase.ExperimentPostDetail {
    return GetExperimentPostDetailUseCase.ExperimentPostDetail(
        experimentPostId = this.id,
        title = this.title,
        uploadDate = this.createdAt.toLocalDate(),
        uploaderName = this.member.name,
        views = this.views,
        recruitStatus = this.recruitStatus,
        summary = this.toSummary(),
        targetGroup = this.targetGroup.toTargetGroup(),
        address = this.toAddress(),
        content = this.content,
        imageList = this.images.map { it.imageUrl },
        isAuthor = this.member.id == memberId,
        isUploaderActive = this.member.deletedAt == null,
        alarmAgree = this.alarmAgree
    )
}

fun ExperimentPost.toSummary(): GetExperimentPostDetailUseCase.ExperimentPostDetail.Summary {
    return GetExperimentPostDetailUseCase.ExperimentPostDetail.Summary(
        startDate = this.startDate,
        endDate = this.endDate,
        leadResearcher = this.leadResearcher,
        matchType = this.matchType,
        reward = this.reward,
        count = this.count,
        timeRequired = this.timeRequired
    )
}

fun TargetGroup.toTargetGroup(): GetExperimentPostDetailUseCase.ExperimentPostDetail.TargetGroup {
    return GetExperimentPostDetailUseCase.ExperimentPostDetail.TargetGroup(
        startAge = this.startAge,
        endAge = this.endAge,
        genderType = this.genderType,
        otherCondition = this.otherCondition
    )
}

fun ExperimentPost.toAddress(): GetExperimentPostDetailUseCase.ExperimentPostDetail.Address {
    return GetExperimentPostDetailUseCase.ExperimentPostDetail.Address(
        place = this.place,
        region = this.region,
        area = this.area,
        detailedAddress = this.detailedAddress
    )
}
