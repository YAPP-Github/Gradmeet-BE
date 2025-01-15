package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.domain.model.experiment.CustomFilter
import com.dobby.backend.domain.model.experiment.Pagination
import com.dobby.backend.infrastructure.database.entity.experiment.ExperimentPostEntity
import org.springframework.stereotype.Repository

@Repository
interface ExperimentPostCustomRepository {
    fun findExperimentPostsByCustomFilter(
        customFilter: CustomFilter,
        pagination: Pagination
    ): List<ExperimentPostEntity>?

    fun findExperimentPostsByMemberIdWithPagination(
        memberId: Long,
        pagination: Pagination
    ): List<ExperimentPostEntity>?
}
