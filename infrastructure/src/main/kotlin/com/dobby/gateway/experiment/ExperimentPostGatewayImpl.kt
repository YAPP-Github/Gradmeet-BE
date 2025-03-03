package com.dobby.gateway.experiment

import com.dobby.model.Pagination
import com.dobby.model.experiment.CustomFilter
import com.dobby.model.experiment.ExperimentPost
import com.dobby.enums.areaInfo.Region
import com.dobby.database.entity.experiment.ExperimentPostEntity
import com.dobby.database.repository.ExperimentPostCustomRepository
import com.dobby.database.repository.ExperimentPostRepository
import com.dobby.model.experiment.ExperimentPostStats
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ExperimentPostGatewayImpl(
    private val experimentPostRepository: ExperimentPostRepository,
    private val experimentPostCustomRepository: ExperimentPostCustomRepository
): ExperimentPostGateway {
    override fun save(experimentPost: ExperimentPost): ExperimentPost {
        val savedEntity = experimentPostRepository
            .save(ExperimentPostEntity.fromDomain(experimentPost))
        return savedEntity.toDomain()
    }

    override fun findExperimentPostsByCustomFilter(
        customFilter: CustomFilter,
        page: Int,
        count: Int,
        order: String
    ): List<ExperimentPost>? {
        val pagination = Pagination(page, count)
        return experimentPostCustomRepository.findExperimentPostsByCustomFilter(
            customFilter,
            pagination,
            order
        )?.map{ it.toDomain()}
    }

    override fun findById(experimentPostId: String): ExperimentPost? {
        return experimentPostRepository
            .findById(experimentPostId)
            .orElse(null)
            ?.let(ExperimentPostEntity::toDomain)
    }

    override fun countExperimentPostsByRegion(region: Region): Int {
        return experimentPostRepository.countByRegion(region)
    }

    override fun countExperimentPostsByRegionAndRecruitStatus(region: Region, recruitStatus: Boolean): Int {
        return experimentPostRepository.countByRegionAndRecruitStatus(region, recruitStatus)
    }

    override fun countExperimentPosts(): Int {
        return experimentPostRepository.count().toInt()
    }

    override fun countExperimentPostsByRecruitStatus(recruitStatus: Boolean): Int {
        return experimentPostRepository.countByRecruitStatus(recruitStatus)
    }

    override fun countExperimentPostByRegionGroupedByArea(region: Region): List<ExperimentPostStats> {
        return experimentPostRepository.countExperimentPostByRegionGroupedByArea(region)
    }

    override fun countExperimentPostByRegionAndRecruitStatusGroupedByArea(region: Region, recruitStatus: Boolean): List<ExperimentPostStats> {
        return experimentPostRepository.countExperimentPostByRegionAndRecruitStatusGroupedByArea(region, recruitStatus)
    }

    override fun countExperimentPostGroupedByRegion(): List<ExperimentPostStats> {
        return experimentPostRepository.countExperimentPostGroupedByRegion()
            .map { row ->
                ExperimentPostStats(
                    regionName = row[0] as? String,
                    areaName = row[1] as? String,
                    count = (row[2] as Number).toLong()
                )
            }
    }



    override fun countExperimentPostsByRecruitStatusGroupedByRegion(recruitStatus: Boolean): List<ExperimentPostStats> {
        return experimentPostRepository.countExperimentPostsByRecruitStatusGroupedByRegion(recruitStatus)
    }

    override fun updateExperimentPostStatus(currentDate: LocalDate): Long {
        return experimentPostCustomRepository.updateExperimentPostStatus(currentDate)
    }

    override fun updateExperimentPost(experimentPost: ExperimentPost): ExperimentPost {
        return experimentPostCustomRepository.updateExperimentPost(experimentPost)
    }

    override fun findExperimentPostsByMemberIdWithPagination(
        memberId: String,
        page: Int,
        count: Int,
        order: String
    ): List<ExperimentPost>? {
        val pagination = Pagination(page, count)
        return experimentPostCustomRepository.findExperimentPostsByMemberIdWithPagination(
            memberId,
            pagination,
            order
        )?.map { it.toDomain() }
    }

    override fun countExperimentPostsByMemberId(memberId: String): Int {
        return experimentPostRepository.countByMemberId(memberId)
    }

    override fun findExperimentPostByMemberIdAndPostId(memberId: String, postId: String): ExperimentPost? {
        return experimentPostRepository
            .findByMemberIdAndId(memberId, postId)
            ?.let(ExperimentPostEntity::toDomain)
    }

    override fun countExperimentPostsByCustomFilter(customFilter: CustomFilter): Int {
        return experimentPostCustomRepository.countExperimentPostsByCustomFilter(customFilter)
    }

    override fun delete(post: ExperimentPost) {
        experimentPostRepository.delete(ExperimentPostEntity.fromDomain(post))
    }

    override fun findMatchingExperimentPosts(): Map<String, List<ExperimentPost>> {
        val matchingPosts = experimentPostCustomRepository.findMatchingExperimentPostsForAllParticipants()
        return matchingPosts.mapValues { (_, postEntities) ->
            postEntities.map { it.toDomain() }
        }
    }
}
