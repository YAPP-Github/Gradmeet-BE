package com.dobby.mapper

import com.dobby.converter.JsonConverter
import com.dobby.model.experiment.ExperimentPostKeywordsLog
import com.dobby.model.experiment.keyword.ExperimentPostKeyword
import com.dobby.persistence.entity.experiment.ExperimentPostKeywordsLogEntity
import com.dobby.persistence.entity.member.MemberEntity
import org.springframework.stereotype.Component

@Component
class ExperimentPostKeywordsLogMapper(
    private val jsonConverter: JsonConverter
) {

    fun toDomain(entity: ExperimentPostKeywordsLogEntity): ExperimentPostKeywordsLog {
        return ExperimentPostKeywordsLog(
            id = entity.id,
            member = entity.member.toDomain(),
            response = jsonConverter.fromJson(entity.response, ExperimentPostKeyword::class.java),
            createdAt = entity.createdAt
        )
    }

    fun fromDomain(domain: ExperimentPostKeywordsLog): ExperimentPostKeywordsLogEntity {
        return ExperimentPostKeywordsLogEntity(
            id = domain.id,
            member = MemberEntity.fromDomain(domain.member),
            response = jsonConverter.toJson(domain.response),
            createdAt = domain.createdAt
        )
    }
}
