package com.dobby.backend.infrastructure.database.repository

import com.dobby.domain.enums.areaInfo.Region
import com.dobby.backend.infrastructure.database.entity.experiment.ExperimentPostEntity
import com.dobby.domain.model.experiment.ExperimentPostStats
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ExperimentPostRepository : JpaRepository<ExperimentPostEntity, String> {
    fun countByRegion(region: Region): Int

    fun countByRegionAndRecruitStatus(region: Region, recruitStatus: Boolean): Int

    @Query("SELECT new com.dobby.domain.model.experiment.ExperimentPostStats(e.region, e.area, COUNT(e)) FROM experiment_post e WHERE e.region = :region GROUP BY e.area")
    fun countExperimentPostByRegionGroupedByArea(@Param("region") region: Region): List<ExperimentPostStats>

    @Query("SELECT new com.dobby.domain.model.experiment.ExperimentPostStats(e.region, e.area, COUNT(e)) FROM experiment_post e WHERE e.region = :region AND e.recruitStatus = :recruitStatus GROUP BY e.area")
    fun countExperimentPostByRegionAndRecruitStatusGroupedByArea(@Param("region") region: Region, @Param("recruitStatus") recruitStatus: Boolean): List<ExperimentPostStats>

    @Query(nativeQuery = true, value = "SELECT e.region AS region, NULL AS area, COUNT(e) AS count FROM experiment_post e GROUP BY e.region")
    fun countExperimentPostGroupedByRegion(): List<Array<Any>>

    fun countByMemberId(memberId: String): Int

    fun countByRecruitStatus(recruitStatus: Boolean): Int

    @Query(nativeQuery = true, value = "SELECT e.region AS region, NULL AS area, COUNT(e) AS count FROM experiment_post e WHERE e.recruitStatus = :recruitStatus GROUP BY e.region")
    fun countExperimentPostsByRecruitStatusGroupedByRegion(@Param("recruitStatus") recruitStatus: Boolean): List<ExperimentPostStats>

    fun findByMemberIdAndId(memberId: String, postId: String): ExperimentPostEntity?
}
