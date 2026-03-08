package com.dobby.persistence.repository

import com.dobby.dto.Pagination
import com.dobby.model.experiment.CustomFilter
import com.dobby.model.experiment.ExperimentPost
import com.dobby.persistence.entity.experiment.ExperimentPostEntity
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ExperimentPostCustomRepository {
    fun findNextPost(experimentPostId: String): ExperimentPostEntity?

    fun findPrevPosts(experimentPostId: String, limit: Int): List<ExperimentPostEntity>

    fun findExperimentPostsByCustomFilter(
        customFilter: CustomFilter,
        pagination: Pagination,
        order: String
    ): List<ExperimentPostEntity>?

    fun updateExperimentPostStatus(currentDate: LocalDate): Long

    fun updateExperimentPost(experimentPost: ExperimentPost): ExperimentPost

    fun findExperimentPostsByMemberIdWithPagination(
        memberId: String,
        pagination: Pagination,
        order: String
    ): List<ExperimentPostEntity>?

    fun countExperimentPostsByCustomFilter(customFilter: CustomFilter): Int

    fun findMatchingExperimentPostsForAllParticipants(): Map<String, List<ExperimentPostEntity>>
}
