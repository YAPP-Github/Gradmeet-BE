package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.experiment.CreateExperimentPostUseCase
import com.dobby.backend.application.usecase.experiment.GetResearcherInfoUseCase
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ExperimentPostService(
    private val createExperimentPostUseCase: CreateExperimentPostUseCase,
    private val getResearcherInfoUseCase: GetResearcherInfoUseCase
) {
    @Transactional
    fun createNewExperimentPost(input: CreateExperimentPostUseCase.Input): CreateExperimentPostUseCase.Output {
        return createExperimentPostUseCase.execute(input)
    }

    fun getDefaultInfo(input: GetResearcherInfoUseCase.Input): GetResearcherInfoUseCase.Output {
        return getResearcherInfoUseCase.execute(input)
    }
}
