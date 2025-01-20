package com.dobby.backend.domain.gateway.experiment

import com.dobby.backend.domain.model.experiment.CustomFilter
import com.dobby.backend.domain.model.experiment.Pagination
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import jakarta.persistence.Tuple
import java.time.LocalDate

interface ExperimentPostGateway {
    fun save(experimentPost: ExperimentPost): ExperimentPost
    fun findExperimentPostsByCustomFilter(customFilter: CustomFilter, pagination: Pagination): List<ExperimentPost>?
    fun findById(experimentPostId: Long): ExperimentPost?
    fun countExperimentPostsByRegion(region: Region): Int
    fun countExperimentPostsByRegionAndRecruitStatus(region: Region, recruitStatus: Boolean): Int
    fun countExperimentPosts(): Int
    fun countExperimentPostsByRecruitStatus(recruitStatus: Boolean): Int
    fun countExperimentPostByRegionGroupedByArea(region: Region): List<Tuple>
    fun countExperimentPostByRegionAndRecruitStatusGroupedByArea(region: Region, recruitStatus: Boolean): List<Tuple>
    fun countExperimentPostGroupedByRegion(): List<Tuple>
    fun countExperimentPostsByRecruitStatusGroupedByRegion(recruitStatus: Boolean): List<Tuple>
    fun updateExperimentPostStatus(currentDate : LocalDate) : Long
    fun findExperimentPostsByMemberIdWithPagination(memberId: Long, pagination: Pagination, order: String): List<ExperimentPost>?
    fun countExperimentPostsByMemberId(memberId: Long): Int
    fun countExperimentPostsByCustomFilter(customFilter: CustomFilter): Int
}
