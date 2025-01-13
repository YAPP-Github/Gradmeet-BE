package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.experiment.*
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ExperimentPostService(
    private val createExperimentPostUseCase: CreateExperimentPostUseCase,
    private val getResearcherInfoUseCase: GetResearcherInfoUseCase,
    private val getExperimentPostDetailUseCase: GetExperimentPostDetailUseCase,
    private val getExperimentPostCountsByRegionUseCase: GetExperimentPostCountsByRegionUseCase,
    private val getExperimentPostCountsByAreaUseCase: GetExperimentPostCountsByAreaUseCase
) {
    @Transactional
    fun createNewExperimentPost(input: CreateExperimentPostUseCase.Input): CreateExperimentPostUseCase.Output {
        return createExperimentPostUseCase.execute(input)
    }

    fun getDefaultInfo(input: GetResearcherInfoUseCase.Input): GetResearcherInfoUseCase.Output {
        return getResearcherInfoUseCase.execute(input)
    }

    fun getExperimentPostDetail(input: GetExperimentPostDetailUseCase.Input): GetExperimentPostDetailUseCase.Output {
        return getExperimentPostDetailUseCase.execute(input)
    }

    fun getExperimentPostCounts(input: Any): Any {
        return when (input) {
            is GetExperimentPostCountsByRegionUseCase.Input -> getExperimentPostCountsByRegionUseCase.execute(input)
            is GetExperimentPostCountsByAreaUseCase.Input -> getExperimentPostCountsByAreaUseCase.execute(input)
            else -> throw IllegalArgumentException("Invalid input type: ${input::class.java.simpleName}")
        }
    }
}
