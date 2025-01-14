package com.dobby.backend.infrastructure.gateway.experiment

import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.CustomFilter
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.domain.model.experiment.Pagination
import com.dobby.backend.infrastructure.converter.ExperimentPostConverter
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import com.dobby.backend.infrastructure.database.entity.experiment.ExperimentPostEntity
import com.dobby.backend.infrastructure.database.repository.ExperimentPostCustomRepository
import com.dobby.backend.infrastructure.database.repository.ExperimentPostRepository
import jakarta.persistence.Tuple
import org.springframework.stereotype.Component

@Component
class ExperimentPostGatewayImpl(
    private val experimentPostRepository: ExperimentPostRepository,
    private val experimentPostCustomRepository: ExperimentPostCustomRepository
): ExperimentPostGateway {
    override fun save(experimentPost: ExperimentPost): ExperimentPost {
        val entity = ExperimentPostConverter.toEntity(experimentPost)
        val savedEntity = experimentPostRepository.save(entity)
        return ExperimentPostConverter.toModel(savedEntity)
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

    override fun findById(experimentPostId: Long): ExperimentPost? {
        return experimentPostRepository
            .findById(experimentPostId)
            .orElse(null)
            ?.let(ExperimentPostEntity::toDomain)
    }

    override fun countExperimentPostsByRegion(region: Region): Int {
        return experimentPostRepository.countByRegion(region)
    }

    override fun countExperimentPosts(): Int {
        return experimentPostRepository.count().toInt()
    }

    override fun countExperimentPostByRegionGroupedByArea(region: Region): List<Tuple> {
        return experimentPostRepository.countExperimentPostByRegionGroupedByArea(region)
    }

    override fun countExperimentPostGroupedByRegion(): List<Tuple> {
        return experimentPostRepository.countExperimentPostGroupedByRegion()
    }
}
