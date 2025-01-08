package com.dobby.backend.domain.model.experiment

import com.dobby.backend.infrastructure.database.entity.enum.MatchType
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import java.time.LocalDate

data class ExperimentPost(
    val id: Long,
    val memberId: Long,
    val targetGroupId: Long,
    val applyMethodId: Long,
    var views: Int,
    val title: String,
    val content: String,
    var researcherName: String,
    val reward: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val durationMinutes: Int,
    val count: Int,
    val matchType: MatchType,
    val univName: String,
    val region: Region,
    val area: Area,
    val detailedAddress: String,
    val alarmAgree: Boolean,
    val images: List<ExperimentImage>
) {
    companion object {
        fun newExperimentPost(
            id: Long,
            memberId: Long,
            targetGroupId: Long,
            applyMethodId: Long,
            views: Int,
            title: String,
            content: String,
            researcherName: String,
            reward: String,
            startDate: LocalDate,
            endDate: LocalDate,
            durationMinutes: Int,
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
            memberId = memberId,
            targetGroupId = targetGroupId,
            applyMethodId = applyMethodId,
            views = views,
            title = title,
            content = content,
            researcherName = researcherName,
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
