package com.dobby.model.experiment

import com.dobby.util.IdGenerator
import com.dobby.model.member.Member
import com.dobby.enums.MatchType
import com.dobby.enums.experiment.TimeSlot
import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import com.dobby.exception.*
import com.dobby.util.TimeProvider
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
    var place: String?,
    var region: Region?,
    var area: Area?,
    var detailedAddress: String?,
    val alarmAgree: Boolean,
    var recruitStatus: Boolean,
    var images: MutableList<ExperimentImage>,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime
) {


    fun incrementViews() {
        this.views += 1
        this.updatedAt = TimeProvider.currentDateTime()
    }

    fun updateRecruitStatus(
        recruitStatus: Boolean = this.recruitStatus
    ): ExperimentPost {
        return this.copy(
            recruitStatus = recruitStatus,
            updatedAt = TimeProvider.currentDateTime()
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
        place: String?,
        region: Region?,
        area: Area?,
        timeRequired: TimeSlot?,
        imageListInfo: List<String>?,
        recruitStatus: Boolean?,
        idGenerator: IdGenerator
    ): ExperimentPost {
        val currentImages = this.images.map { it.imageUrl }.toSet()
        val newImages = imageListInfo?.takeIf { it.isNotEmpty() } ?: emptyList()

        validate(title, reward, content, leadResearcher, matchType, place, region, area, count, newImages)

        val updatedImages = if(currentImages == newImages) {
            this.images
        } else {
            newImages.map { imageUrl ->
                ExperimentImage(
                    id = idGenerator.generateId(),
                    experimentPost = this,
                    imageUrl = imageUrl
                )
            }.toMutableList()
        }

        return this.copy(
            targetGroup = targetGroup?.let { this.targetGroup.update(it.startAge, it.endAge, it.genderType, it.otherCondition) } ?: this.targetGroup,
            applyMethod = applyMethod?.let { this.applyMethod.update(it.phoneNum, it.formUrl, it.content) } ?: this.applyMethod,
            title = title?: this.title,
            reward = reward?: this.reward,
            startDate = startDate?: this.startDate,
            endDate = endDate?: this.endDate,
            content = content?: this.content,
            count = count?: this.count,
            leadResearcher = leadResearcher?: this.leadResearcher,
            detailedAddress = detailedAddress,
            matchType = matchType ?: this.matchType,
            timeRequired = timeRequired ?: this.timeRequired,
            place = place,
            region = region,
            area = area,
            images = updatedImages,
            updatedAt = TimeProvider.currentDateTime(),
            recruitStatus = recruitStatus ?: this.recruitStatus
        )
    }

    fun updateImages(newImages: List<ExperimentImage>) {
        if(newImages.size > 3) {
            throw ExperimentPostImageSizeException
        }
        images.clear()
        images.addAll(newImages)
        updatedAt = TimeProvider.currentDateTime()
    }


    companion object {
        fun newExperimentPost(
            id: String,
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
            place: String?,
            region: Region?,
            area: Area?,
            detailedAddress: String?,
            alarmAgree: Boolean,
            recruitStatus: Boolean,
            images: List<ExperimentImage> = listOf(),
        ): ExperimentPost {
            validate(title, reward, content, leadResearcher, matchType, place, region, area, count, images = images.map { it.imageUrl })

            return ExperimentPost(
                id = id,
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
                place = place,
                region = region,
                area = area,
                detailedAddress = detailedAddress,
                alarmAgree = alarmAgree,
                recruitStatus = recruitStatus,
                images = images.toMutableList(),
                createdAt = TimeProvider.currentDateTime(),
                updatedAt = TimeProvider.currentDateTime()
            )
        }
        private fun validate(
            title: String?,
            reward: String?,
            content: String?,
            leadResearcher: String?,
            matchType: MatchType?,
            place: String?,
            region: Region?,
            area: Area?,
            count: Int?,
            images: List<String>?
        ) {
            if (images != null && images.size > 3) throw ExperimentPostImageSizeException
            if (title == null) throw ExperimentPostTitleException
            if (reward == null) throw ExperimentPostRewardException
            if (content == null) throw ExperimentPostContentException
            if (leadResearcher == null) throw ExperimentPostLeadResearcherException
            if (count == null || count <= 0) throw ExperimentPostCountException
            if (matchType == MatchType.ONLINE && listOf(place, region, area).any { it != null }) {
                throw ExperimentPostInvalidOnlineRequestException
            }
        }
    }
}
