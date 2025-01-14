package com.dobby.backend.domain.gateway.experiment

import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import jakarta.persistence.Tuple

interface ExperimentPostGateway {
    fun save(experimentPost: ExperimentPost): ExperimentPost
    fun findExperimentPostsByCustomFilter(
        customFilter: com.dobby.backend.domain.model.experiment.CustomFilter,
        pagination: com.dobby.backend.domain.model.experiment.Pagination): List<ExperimentPost>?
    fun findById(experimentPostId: Long): ExperimentPost?
    fun countExperimentPostsByRegion(region: Region): Int
    fun countExperimentPosts(): Int
    fun countExperimentPostByRegionGroupedByArea(region: Region): List<Tuple>
    fun countExperimentPostGroupedByRegion(): List<Tuple>
}
