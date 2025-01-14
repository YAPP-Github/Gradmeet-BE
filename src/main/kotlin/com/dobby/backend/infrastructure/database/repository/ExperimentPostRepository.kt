package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import com.dobby.backend.infrastructure.database.entity.experiment.ExperimentPostEntity
import jakarta.persistence.Tuple
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ExperimentPostRepository : JpaRepository<ExperimentPostEntity, Long> {
    fun countByRegion(region: Region): Int

    @Query("SELECT e.area, COUNT(e) FROM experiment_post e WHERE e.region = :region GROUP BY e.area")
    fun countExperimentPostByRegionGroupedByArea(@Param("region") region: Region): List<Tuple>

    @Query("SELECT e.region, COUNT(e) FROM experiment_post e GROUP BY e.region")
    fun countExperimentPostGroupedByRegion(): List<Tuple>
}
