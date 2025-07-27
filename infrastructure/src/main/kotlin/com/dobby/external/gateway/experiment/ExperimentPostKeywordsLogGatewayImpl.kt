package com.dobby.external.gateway.experiment

import com.dobby.gateway.experiment.ExperimentPostKeywordsLogGateway
import com.dobby.model.experiment.ExperimentPostKeywordsLog
import com.dobby.persistence.entity.experiment.ExperimentPostKeywordsLogEntity
import com.dobby.persistence.repository.ExperimentPostKeywordsLogRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ExperimentPostKeywordsLogGatewayImpl(
    private val experimentPostKeywordsLogRepository: ExperimentPostKeywordsLogRepository
) : ExperimentPostKeywordsLogGateway {
    override fun save(experimentPostKeywordsLog: ExperimentPostKeywordsLog): ExperimentPostKeywordsLog {
        val savedEntity = experimentPostKeywordsLogRepository
            .save(ExperimentPostKeywordsLogEntity.fromDomain(experimentPostKeywordsLog))
        return savedEntity.toDomain()
    }

    override fun countByMemberIdAndCreatedAtBetween(
        memberId: String,
        start: LocalDateTime,
        end: LocalDateTime
    ): Int {
        return experimentPostKeywordsLogRepository.countByMemberIdAndCreatedAtBetween(memberId, start, end)
    }
}
