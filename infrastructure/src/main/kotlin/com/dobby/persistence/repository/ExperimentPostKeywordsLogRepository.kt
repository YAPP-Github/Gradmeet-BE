package com.dobby.persistence.repository

import com.dobby.persistence.entity.experiment.ExperimentPostKeywordsLogEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ExperimentPostKeywordsLogRepository : JpaRepository<ExperimentPostKeywordsLogEntity, String> {
}
