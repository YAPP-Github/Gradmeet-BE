package com.dobby.backend.domain.gateway.experiment

import com.dobby.backend.domain.model.experiment.CustomFilter
import com.dobby.backend.domain.model.experiment.Pagination
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import jakarta.persistence.Tuple
import java.time.LocalDate

interface ExperimentPostGateway {
    fun save(experimentPost: ExperimentPost): ExperimentPost
    fun findExperimentPostsByCustomFilter(
        customFilter: CustomFilter, pagination:Pagination): List<ExperimentPost>?
    fun findById(experimentPostId: Long): ExperimentPost?
    fun countExperimentPostsByRegion(region: Region): Int
    fun countExperimentPosts(): Int
    fun countExperimentPostByRegionGroupedByArea(region: Region): List<Tuple>
    fun countExperimentPostGroupedByRegion(): List<Tuple>
    fun updateExperimentPostStatus(todayDate : LocalDate) : Long
}
