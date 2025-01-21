package com.dobby.backend.infrastructure.converter

import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.infrastructure.database.entity.experiment.ApplyMethodEntity
import com.dobby.backend.infrastructure.database.entity.experiment.ExperimentPostEntity
import com.dobby.backend.infrastructure.database.entity.experiment.TargetGroupEntity
import com.dobby.backend.infrastructure.database.entity.member.MemberEntity

object ExperimentPostConverter{

    fun toModel(entity: ExperimentPostEntity): ExperimentPost{
        return ExperimentPost(
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
            images = emptyList(), // 이미지 업로드 부분 보류,
            recruitStatus = entity.recruitStatus,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    fun toEntity(model: ExperimentPost): ExperimentPostEntity{
        return ExperimentPostEntity(
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
            images = emptyList(), // 이미지 업로드 부분 보류,
            recruitStatus = model.recruitStatus,
            createdAt = model.createdAt,
            updatedAt = model.updatedAt
        )
    }
}
