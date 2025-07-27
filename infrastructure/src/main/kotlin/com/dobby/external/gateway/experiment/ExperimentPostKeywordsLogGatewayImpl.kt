package com.dobby.external.gateway.experiment

import com.dobby.gateway.experiment.ExperimentPostKeywordsLogGateway
import com.dobby.mapper.ExperimentPostKeywordsLogMapper
import com.dobby.model.experiment.ExperimentPostKeywordsLog
import com.dobby.persistence.repository.ExperimentPostKeywordsLogRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ExperimentPostKeywordsLogGatewayImpl(
    private val experimentPostKeywordsLogRepository: ExperimentPostKeywordsLogRepository,
    private val mapper: ExperimentPostKeywordsLogMapper
) : ExperimentPostKeywordsLogGateway {

    override fun save(experimentPostKeywordsLog: ExperimentPostKeywordsLog): ExperimentPostKeywordsLog {
        val entity = mapper.fromDomain(experimentPostKeywordsLog)
        val savedEntity = experimentPostKeywordsLogRepository.save(entity)
        return mapper.toDomain(savedEntity)
    }

    override fun countByMemberIdAndCreatedAtBetween(
        memberId: String,
        start: LocalDateTime,
        end: LocalDateTime
    ): Int {
        return experimentPostKeywordsLogRepository.countByMemberIdAndCreatedAtBetween(memberId, start, end)
    }
}
