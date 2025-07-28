package com.dobby.persistence.repository

import com.dobby.persistence.entity.experiment.ExperimentPostKeywordsLogEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ExperimentPostKeywordsLogRepository : JpaRepository<ExperimentPostKeywordsLogEntity, String> {
    fun countByMemberIdAndCreatedAtBetween(memberId: String, start: LocalDateTime, end: LocalDateTime): Int
}
