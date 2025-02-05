package com.dobby.backend.infrastructure.gateway.experiment

import com.dobby.backend.application.model.Pagination
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.CustomFilter
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import com.dobby.backend.infrastructure.database.entity.experiment.ExperimentPostEntity
import com.dobby.backend.infrastructure.database.repository.ExperimentPostCustomRepository
import com.dobby.backend.infrastructure.database.repository.ExperimentPostRepository
import jakarta.persistence.Tuple
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
        pagination: Pagination
    ): List<ExperimentPost>? {
        return experimentPostCustomRepository.findExperimentPostsByCustomFilter(
            customFilter,
            pagination
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

    override fun countExperimentPostByRegionGroupedByArea(region: Region): List<Tuple> {
        return experimentPostRepository.countExperimentPostByRegionGroupedByArea(region)
    }

    override fun countExperimentPostByRegionAndRecruitStatusGroupedByArea(region: Region, recruitStatus: Boolean): List<Tuple> {
        return experimentPostRepository.countExperimentPostByRegionAndRecruitStatusGroupedByArea(region, recruitStatus)
    }

    override fun countExperimentPostGroupedByRegion(): List<Tuple> {
        return experimentPostRepository.countExperimentPostGroupedByRegion()
    }

    override fun countExperimentPostsByRecruitStatusGroupedByRegion(recruitStatus: Boolean): List<Tuple> {
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
        pagination: Pagination,
        order: String
    ): List<ExperimentPost>? {
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
}
