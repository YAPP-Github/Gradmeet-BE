package com.dobby.backend.domain.model.experiment

import com.dobby.backend.domain.exception.ExperimentPostImageSizeException
import com.dobby.backend.domain.exception.ExperimentPostInvalidOnlineRequestException
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import com.dobby.backend.util.generateTSID
import java.time.LocalDate
import java.time.LocalDateTime

data class ExperimentPost(
    val id: String,
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
    var timeRequired: TimeSlot?,
    var count: Int,
    var matchType: MatchType,
    var univName: String?,
    var region: Region?,
    var area: Area?,
    var detailedAddress: String?,
    val alarmAgree: Boolean,
    var recruitStatus: Boolean,
    var images: MutableList<ExperimentImage>,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime
) {
    init {
        validate()
    }

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

    fun update(
        targetGroup: TargetGroup?,
        applyMethod: ApplyMethod?,
        title: String?,
        reward: String?,
        startDate: LocalDate?,
        endDate: LocalDate?,
        content: String?,
        count: Int?,
        leadResearcher: String?,
        detailedAddress: String?,
        matchType: MatchType?,
        univName: String?,
        region: Region?,
        area: Area?,
        imageListInfo: List<String>?
    ): ExperimentPost {
        val updatedImages = imageListInfo?.map { imageUrl ->
            val existingImage = this.images.find { it.imageUrl == imageUrl }
            existingImage ?: ExperimentImage(id = "0", experimentPost = this, imageUrl = imageUrl)
        } ?: this.images

        return this.copy(
            targetGroup = targetGroup ?: this.targetGroup,
            applyMethod = applyMethod ?: this.applyMethod,
            title = title ?: this.title,
            reward = reward ?: this.reward,
            startDate = startDate ?: this.startDate,
            endDate = endDate ?: this.endDate,
            content = content ?: this.content,
            count = count ?: this.count,
            leadResearcher = leadResearcher ?: this.leadResearcher,
            detailedAddress = detailedAddress ?: this.detailedAddress,
            matchType = matchType ?: this.matchType,
            univName = univName ?: this.univName,
            region = region ?: this.region,
            area = area ?: this.area,
            images = updatedImages.toMutableList(),
            updatedAt = LocalDateTime.now()
        ).apply { validate() }
    }


    fun updateImages(newImages: List<ExperimentImage>) {
        require(newImages.size <= 3) {
            throw ExperimentPostImageSizeException
        }
        images.clear()
        images.addAll(newImages)
        updatedAt = LocalDateTime.now()
    }


    companion object {
        fun newExperimentPost(
            member: Member,
            targetGroup: TargetGroup,
            applyMethod: ApplyMethod,
            title: String,
            content: String,
            leadResearcher: String,
            reward: String,
            startDate: LocalDate?,
            endDate: LocalDate?,
            timeRequired: TimeSlot?,
            count: Int,
            matchType: MatchType,
            univName: String?,
            region: Region?,
            area: Area?,
            detailedAddress: String?,
            alarmAgree: Boolean,
            recruitStatus: Boolean,
            images: List<ExperimentImage> = listOf(),
        ) = ExperimentPost(
            id = generateTSID(),
            member = member,
            targetGroup = targetGroup,
            applyMethod = applyMethod,
            views = 0,
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
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    private fun validate() {
        if (matchType == MatchType.ONLINE && listOf(univName, region, area).any { it != null }) {
            throw ExperimentPostInvalidOnlineRequestException
        }
    }
}
