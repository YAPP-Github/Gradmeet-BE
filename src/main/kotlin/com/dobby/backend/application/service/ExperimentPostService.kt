package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.experiment.GenerateExperimentPostPreSignedUrlUseCase
import com.dobby.backend.application.usecase.experiment.*
import com.dobby.backend.application.usecase.experiment.CreateExperimentPostUseCase
import com.dobby.backend.application.usecase.experiment.GetExperimentPostApplyMethodUseCase
import com.dobby.backend.application.usecase.experiment.GetExperimentPostDetailUseCase
import com.dobby.backend.domain.exception.ExperimentAreaInCorrectException
import com.dobby.backend.domain.exception.ExperimentAreaOverflowException
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ExperimentPostService(
    private val createExperimentPostUseCase: CreateExperimentPostUseCase,
    private val updateExperimentPostUseCase: UpdateExperimentPostUseCase,
    private val getExperimentPostsUseCase: GetExperimentPostsUseCase,
    private val getExperimentPostDetailUseCase: GetExperimentPostDetailUseCase,
    private val getExperimentPostCountsByRegionUseCase: GetExperimentPostCountsByRegionUseCase,
    private val getExperimentPostCountsByAreaUseCase: GetExperimentPostCountsByAreaUseCase,
    private val getExperimentPostApplyMethodUseCase: GetExperimentPostApplyMethodUseCase,
    private val generateExperimentPostPreSignedUrlUseCase: GenerateExperimentPostPreSignedUrlUseCase,
    private val updateExpiredExperimentPostUseCase: UpdateExpiredExperimentPostUseCase
) {
    @Transactional
    fun createNewExperimentPost(input: CreateExperimentPostUseCase.Input): CreateExperimentPostUseCase.Output {
        return createExperimentPostUseCase.execute(input)
    }

    @Transactional
    fun updateExperimentPost(input: UpdateExperimentPostUseCase.Input): UpdateExperimentPostUseCase.Output{
        return updateExperimentPostUseCase.execute(input)
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

    @Transactional
    fun updateExpiredExperimentPosts(input: UpdateExpiredExperimentPostUseCase.Input): UpdateExpiredExperimentPostUseCase.Output  {
        return updateExpiredExperimentPostUseCase.execute(input)
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
    fun generatePreSignedUrl(input: GenerateExperimentPostPreSignedUrlUseCase.Input): GenerateExperimentPostPreSignedUrlUseCase.Output {
        return generateExperimentPostPreSignedUrlUseCase.execute(input)
    }
}
