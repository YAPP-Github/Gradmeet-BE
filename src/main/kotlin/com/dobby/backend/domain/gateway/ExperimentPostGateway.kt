package com.dobby.backend.domain.gateway

import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import jakarta.persistence.Tuple

interface ExperimentPostGateway {
    fun save(experimentPost: ExperimentPost): ExperimentPost
    fun findById(experimentPostId: Long): ExperimentPost?
    fun countExperimentPostsByRegion(region: Region): Int
    fun countExperimentPosts(): Int
    fun countExperimentPostByRegionGroupedByArea(region: Region): List<Tuple>
    fun countExperimentPostGroupedByRegion(): List<Tuple>
}
