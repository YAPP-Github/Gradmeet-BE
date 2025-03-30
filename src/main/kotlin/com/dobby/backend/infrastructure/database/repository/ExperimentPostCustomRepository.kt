package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.application.model.Pagination
import com.dobby.model.experiment.CustomFilter
import com.dobby.model.experiment.ExperimentPost
import com.dobby.backend.infrastructure.database.entity.experiment.ExperimentPostEntity
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ExperimentPostCustomRepository {
    fun findExperimentPostsByCustomFilter(
        customFilter: CustomFilter,
        pagination: Pagination,
        order: String
    ): List<ExperimentPostEntity>?

    fun updateExperimentPostStatus(currentDate : LocalDate): Long

    fun updateExperimentPost(experimentPost: ExperimentPost): ExperimentPost

    fun findExperimentPostsByMemberIdWithPagination(
        memberId: String,
        pagination: Pagination,
        order: String
    ): List<ExperimentPostEntity>?

    fun countExperimentPostsByCustomFilter(customFilter: CustomFilter): Int

    fun findMatchingExperimentPostsForAllParticipants(): Map<String, List<ExperimentPostEntity>>
}
