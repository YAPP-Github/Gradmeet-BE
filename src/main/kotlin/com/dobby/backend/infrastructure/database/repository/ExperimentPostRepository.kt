package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.experiment.ExperimentPostEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ExperimentPostRepository : JpaRepository<ExperimentPostEntity, Long> {
}
