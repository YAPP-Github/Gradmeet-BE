package com.dobby.backend.domain.model.experiment

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
    val title: String,
    val content: String,
    var leadResearcher: String,
    val reward: String,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val timeRequired: TimeSlot?,
    val count: Int,
    val matchType: MatchType,
    val univName: String,
    val region: Region,
    val area: Area,
    val detailedAddress: String?,
    val alarmAgree: Boolean,
    var recruitStatus: Boolean,
    val images: List<ExperimentImage>,
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
            images: List<ExperimentImage>,
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
            images = images,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
