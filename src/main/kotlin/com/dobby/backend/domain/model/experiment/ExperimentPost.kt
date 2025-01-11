package com.dobby.backend.domain.model.experiment

import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enum.MatchType
import com.dobby.backend.infrastructure.database.entity.enum.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import java.time.LocalDate

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
    val startDate: LocalDate,
    val endDate: LocalDate,
    val durationMinutes: TimeSlot,
    val count: Int,
    val matchType: MatchType,
    val univName: String,
    val region: Region,
    val area: Area,
    val detailedAddress: String?,
    val alarmAgree: Boolean,
    val images: List<ExperimentImage>
) {

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
            startDate: LocalDate,
            endDate: LocalDate,
            durationMinutes: TimeSlot,
            count: Int,
            matchType: MatchType,
            univName: String,
            region: Region,
            area: Area,
            detailedAddress: String,
            alarmAgree: Boolean,
            images: List<ExperimentImage>
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
            durationMinutes = durationMinutes,
            count = count,
            matchType = matchType,
            univName = univName,
            region = region,
            area = area,
            detailedAddress = detailedAddress,
            alarmAgree = alarmAgree,
            images = images
        )
    }
}
