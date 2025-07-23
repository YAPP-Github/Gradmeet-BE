package com.dobby.usecase.experiment

import com.dobby.gateway.experiment.ExperimentKeywordExtractionGateway
import com.dobby.model.experiment.keyword.ExperimentPostKeyword
import com.dobby.usecase.UseCase

class ExtractExperimentPostKeywordsUseCase(
    private val experimentKeywordExtractionGateway: ExperimentKeywordExtractionGateway
) : UseCase<ExtractExperimentPostKeywordsUseCase.Input, ExtractExperimentPostKeywordsUseCase.Output> {
    data class Input(val text: String)
    data class Output(val experimentPostKeyword: ExperimentPostKeyword)

    override fun execute(input: Input): Output {
        val experimentPostKeyword = experimentKeywordExtractionGateway.extractKeywords(input.text)
        return Output(experimentPostKeyword)
    }
}
