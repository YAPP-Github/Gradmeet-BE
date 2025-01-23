package com.dobby.backend.domain.model.experiment

import com.dobby.backend.application.usecase.experiment.UpdateExperimentPostUseCase
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import java.time.LocalDate
import java.time.LocalDateTime

data class ExperimentPost(
    val id: Long,
    val member: Member,
    val targetGroup: TargetGroup,
    val applyMethod: ApplyMethod,
    var views: Int,
    var title: String,
    var content: String,
    var leadResearcher: String,
    var reward: String,
    var startDate: LocalDate?,
    var endDate: LocalDate?,
    val timeRequired: TimeSlot?,
    var count: Int,
    var matchType: MatchType,
    var univName: String,
    val region: Region,
    val area: Area,
    var detailedAddress: String?,
    val alarmAgree: Boolean,
    var recruitStatus: Boolean,
    var images: MutableList<ExperimentImage>,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime
) {
    fun incrementViews() {
        this.views += 1
        this.updatedAt = LocalDateTime.now()
    }

    fun updateRecruitStatus(
        recruitStatus: Boolean = this.recruitStatus,
        updatedAt: LocalDateTime = this.updatedAt
    ): ExperimentPost {
        return this.copy(
            recruitStatus = recruitStatus,
            updatedAt = updatedAt
        )
    }

    fun updateImages(newImages: List<ExperimentImage>) {
        images.clear()
        images.addAll(newImages)
        updatedAt = LocalDateTime.now()
    }

    fun updateDetails(
        targetGroupInfo: UpdateExperimentPostUseCase.TargetGroupInfo?,
        applyMethodInfo: UpdateExperimentPostUseCase.ApplyMethodInfo?,
        updatedAt: LocalDateTime
    ) {
        targetGroupInfo?.let {
            targetGroup.update(
                startAge = it.startAge,
                endAge = it.endAge,
                genderType = it.genderType,
                otherCondition = it.otherCondition
            )
        }

        applyMethodInfo?.let {
            applyMethod.update(
                content = it.content,
                formUrl = it.formUrl,
                phoneNum = it.phoneNum
            )
        }

        this.updatedAt = updatedAt
    }

    companion object {
        fun newExperimentPost(
            id: Long,
            member: Member,
            targetGroup: TargetGroup,
            applyMethod: ApplyMethod,
            views: Int,
            title: String,
            content: String,
            leadResearcher: String,
            reward: String,
            startDate: LocalDate?,
            endDate: LocalDate?,
            timeRequired: TimeSlot?,
            count: Int,
            matchType: MatchType,
            univName: String,
            region: Region,
            area: Area,
            detailedAddress: String,
            alarmAgree: Boolean,
            recruitStatus: Boolean,
            images: List<ExperimentImage> = listOf(),
            createdAt: LocalDateTime = LocalDateTime.now(),
            updatedAt: LocalDateTime = LocalDateTime.now()
        ) = ExperimentPost(
            id = id,
            member = member,
            targetGroup = targetGroup,
            applyMethod = applyMethod,
            views = views,
            title = title,
            content = content,
            leadResearcher = leadResearcher,
            reward = reward,
            startDate = startDate,
            endDate = endDate,
            timeRequired = timeRequired,
            count = count,
            matchType = matchType,
            univName = univName,
            region = region,
            area = area,
            detailedAddress = detailedAddress,
            alarmAgree = alarmAgree,
            recruitStatus = recruitStatus,
            images = images.toMutableList(),
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
