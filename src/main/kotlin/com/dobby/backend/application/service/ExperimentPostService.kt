package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.experiment.*
import com.dobby.backend.application.usecase.experiment.CreateExperimentPostUseCase
import com.dobby.backend.application.usecase.experiment.GetExperimentPostApplyMethodUseCase
import com.dobby.backend.application.usecase.experiment.GetExperimentPostDetailUseCase
import com.dobby.backend.application.usecase.member.GetResearcherInfoUseCase
import com.dobby.backend.domain.exception.ExperimentAreaInCorrectException
import com.dobby.backend.domain.exception.ExperimentAreaOverflowException
import com.dobby.backend.domain.exception.ExperimentAreaSelectionException
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area.Companion.isAll
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ExperimentPostService(
    private val createExperimentPostUseCase: CreateExperimentPostUseCase,
    private val getExperimentPostsUseCase: GetExperimentPostsUseCase,
    private val getExperimentPostDetailUseCase: GetExperimentPostDetailUseCase,
    private val getExperimentPostCountsByRegionUseCase: GetExperimentPostCountsByRegionUseCase,
    private val getExperimentPostCountsByAreaUseCase: GetExperimentPostCountsByAreaUseCase,
    private val getExperimentPostApplyMethodUseCase: GetExperimentPostApplyMethodUseCase,
) {
    @Transactional
    fun createNewExperimentPost(input: CreateExperimentPostUseCase.Input): CreateExperimentPostUseCase.Output {
        return createExperimentPostUseCase.execute(input)
    }

    @Transactional
    fun getExperimentPosts(input: GetExperimentPostsUseCase.Input): List<GetExperimentPostsUseCase.Output> {
        validateFilter(input)
        return getExperimentPostsUseCase.execute(input)
    }

    @Transactional
    fun getExperimentPostDetail(input: GetExperimentPostDetailUseCase.Input): GetExperimentPostDetailUseCase.Output {
        return getExperimentPostDetailUseCase.execute(input)
    }

    @Transactional
    fun getExperimentPostApplyMethod(input: GetExperimentPostApplyMethodUseCase.Input): GetExperimentPostApplyMethodUseCase.Output {
        return getExperimentPostApplyMethodUseCase.execute(input)
    }

    fun getExperimentPostCounts(input: Any): Any {
        return when (input) {
            is GetExperimentPostCountsByRegionUseCase.Input -> getExperimentPostCountsByRegionUseCase.execute(input)
            is GetExperimentPostCountsByAreaUseCase.Input -> getExperimentPostCountsByAreaUseCase.execute(input)
            else -> throw IllegalArgumentException("Invalid input type: ${input::class.java.simpleName}")
        }
    }

    fun validateFilter(input: GetExperimentPostsUseCase.Input) {
        val locationInfo = input.customFilter.locationTarget ?: return

        locationInfo.areas?.let { validateLocationAreaCount(it) }
        validateRegion(locationInfo)
    }

    private fun validateLocationAreaCount(areas: List<Area>) {
        if (areas.size > 5) throw ExperimentAreaOverflowException()
    }

    private fun validateRegion(locationInfo: GetExperimentPostsUseCase.LocationTargetInput){
        val region = locationInfo.region?: return
        val validAreas = Area.findByRegion(region).map { it.name }

        if(locationInfo.areas?.map {it.name }?.any {it !in validAreas } == true) {
            throw ExperimentAreaInCorrectException()
        }
    }
}
