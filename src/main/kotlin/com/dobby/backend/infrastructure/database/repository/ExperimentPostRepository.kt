package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.domain.enums.areaInfo.Region
import com.dobby.backend.infrastructure.database.entity.experiment.ExperimentPostEntity
import jakarta.persistence.Tuple
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ExperimentPostRepository : JpaRepository<ExperimentPostEntity, String> {
    fun countByRegion(region: Region): Int

    fun countByRegionAndRecruitStatus(region: Region, recruitStatus: Boolean): Int

    @Query("SELECT e.area, COUNT(e) FROM experiment_post e WHERE e.region = :region GROUP BY e.area")
    fun countExperimentPostByRegionGroupedByArea(@Param("region") region: Region): List<Tuple>

    @Query("SELECT e.area, COUNT(e) FROM experiment_post e WHERE e.region = :region AND e.recruitStatus = :recruitStatus GROUP BY e.area")
    fun countExperimentPostByRegionAndRecruitStatusGroupedByArea(region: Region, recruitStatus: Boolean): List<Tuple>

    @Query("SELECT e.region, COUNT(e) FROM experiment_post e GROUP BY e.region")
    fun countExperimentPostGroupedByRegion(): List<Tuple>

    fun countByMemberId(memberId: String): Int

    fun countByRecruitStatus(recruitStatus: Boolean): Int

    @Query("SELECT e.region, COUNT(e) FROM experiment_post e WHERE e.recruitStatus = :recruitStatus GROUP BY e.region")
    fun countExperimentPostsByRecruitStatusGroupedByRegion(recruitStatus: Boolean): List<Tuple>

    fun findByMemberIdAndId(memberId: String, postId: String): ExperimentPostEntity?
}
