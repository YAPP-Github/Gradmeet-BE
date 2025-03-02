package com.dobby.gateway.experiment

import com.dobby.model.experiment.CustomFilter
import com.dobby.model.experiment.ExperimentPost
import com.dobby.enums.areaInfo.Region
import com.dobby.model.experiment.ExperimentPostStats
import java.time.LocalDate

interface ExperimentPostGateway {
    fun save(experimentPost: ExperimentPost): ExperimentPost
    fun updateExperimentPost(experimentPost: ExperimentPost): ExperimentPost
    fun findExperimentPostsByCustomFilter(customFilter: CustomFilter, page: Int, count: Int, order: String): List<ExperimentPost>?
    fun findById(experimentPostId: String): ExperimentPost?
    fun countExperimentPostsByRegion(region: Region): Int
    fun countExperimentPostsByRegionAndRecruitStatus(region: Region, recruitStatus: Boolean): Int
    fun countExperimentPosts(): Int
    fun countExperimentPostsByRecruitStatus(recruitStatus: Boolean): Int
    fun countExperimentPostByRegionGroupedByArea(region: Region): List<ExperimentPostStats>
    fun countExperimentPostByRegionAndRecruitStatusGroupedByArea(region: Region, recruitStatus: Boolean): List<ExperimentPostStats>
    fun countExperimentPostGroupedByRegion(): List<ExperimentPostStats>
    fun countExperimentPostsByRecruitStatusGroupedByRegion(recruitStatus: Boolean): List<ExperimentPostStats>
    fun updateExperimentPostStatus(currentDate : LocalDate) : Long
    fun findExperimentPostsByMemberIdWithPagination(memberId: String, page: Int, count: Int, order: String): List<ExperimentPost>?
    fun countExperimentPostsByMemberId(memberId: String): Int
    fun findExperimentPostByMemberIdAndPostId(memberId: String, postId: String): ExperimentPost?
    fun countExperimentPostsByCustomFilter(customFilter: CustomFilter): Int
    fun delete(post: ExperimentPost)
    fun findMatchingExperimentPosts(): Map<String, List<ExperimentPost>>
}
