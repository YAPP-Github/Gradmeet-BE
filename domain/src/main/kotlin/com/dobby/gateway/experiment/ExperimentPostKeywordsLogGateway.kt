package com.dobby.gateway.experiment

import com.dobby.model.experiment.ExperimentPostKeywordsLog
import java.time.LocalDateTime

interface ExperimentPostKeywordsLogGateway {
    fun save(experimentPostKeywordsLog: ExperimentPostKeywordsLog): ExperimentPostKeywordsLog
    fun countByMemberIdAndCreatedAtBetween(memberId: String, start: LocalDateTime, end: LocalDateTime): Int
}
