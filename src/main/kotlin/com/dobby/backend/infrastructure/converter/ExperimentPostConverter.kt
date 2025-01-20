package com.dobby.backend.infrastructure.converter

import com.dobby.backend.domain.model.experiment.ExperimentImage
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.infrastructure.database.entity.experiment.ApplyMethodEntity
import com.dobby.backend.infrastructure.database.entity.experiment.ExperimentImageEntity
import com.dobby.backend.infrastructure.database.entity.experiment.ExperimentPostEntity
import com.dobby.backend.infrastructure.database.entity.experiment.TargetGroupEntity
import com.dobby.backend.infrastructure.database.entity.member.MemberEntity

object ExperimentPostConverter{

    fun toModel(entity: ExperimentPostEntity): ExperimentPost {
        val experimentPost = ExperimentPost(
            id = entity.id,
            views = entity.views,
            startDate = entity.startDate,
            endDate = entity.endDate,
            title = entity.title,
            content = entity.content,
            reward = entity.reward,
            univName = entity.univName,
            leadResearcher = entity.leadResearcher,
            region = entity.region,
            area = entity.area,
            count = entity.count,
            member = entity.member.toDomain(),
            alarmAgree = entity.alarmAgree,
            detailedAddress = entity.detailedAddress,
            applyMethod = entity.applyMethod.toDomain(),
            timeRequired = entity.timeRequired,
            matchType = entity.matchType,
            targetGroup = entity.targetGroup.toDomain(),
            images = mutableListOf(),
            recruitStatus = entity.recruitStatus,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
        experimentPost.images = entity.images.map { experimentImageEntity ->
            ExperimentImage(
                id = experimentImageEntity.id,
                imageUrl = experimentImageEntity.imageUrl,
                experimentPost = experimentPost
            )
        }
        return experimentPost
    }

    fun toEntity(model: ExperimentPost): ExperimentPostEntity{
        val experimentPostEntity = ExperimentPostEntity(
                member = MemberEntity.fromDomain(model.member),
                targetGroup = TargetGroupEntity.fromDomain(model.targetGroup),
                applyMethod = ApplyMethodEntity.fromDomain(model.applyMethod),
                id = model.id,
                views = model.views,
                startDate = model.startDate,
                endDate = model.endDate,
                title = model.title,
                content = model.content,
                reward = model.reward,
                univName = model.univName,
                leadResearcher = model.leadResearcher,
                region = model.region,
                area = model.area,
                count = model.count,
                alarmAgree = model.alarmAgree,
                detailedAddress = model.detailedAddress,
                timeRequired = model.timeRequired,
                matchType = model.matchType,
                images = mutableListOf(),
                createdAt = model.createdAt,
                updatedAt = model.updatedAt
        )

        val images = model.images.map { image ->
            ExperimentImageEntity(
                id = image.id,
                imageUrl = image.imageUrl,
                experimentPost = experimentPostEntity
            )
        }

        experimentPostEntity.images = images.toMutableList()

        return experimentPostEntity
    }
}
