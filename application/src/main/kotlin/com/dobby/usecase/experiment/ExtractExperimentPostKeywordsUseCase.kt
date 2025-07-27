package com.dobby.usecase.experiment

import com.dobby.gateway.experiment.ExperimentKeywordExtractionGateway
import com.dobby.gateway.experiment.ExperimentPostKeywordsLogGateway
import com.dobby.gateway.member.MemberGateway
import com.dobby.model.experiment.ExperimentPostKeywordsLog
import com.dobby.model.experiment.keyword.ExperimentPostKeyword
import com.dobby.usecase.UseCase
import com.dobby.util.IdGenerator

class ExtractExperimentPostKeywordsUseCase(
    private val experimentKeywordExtractionGateway: ExperimentKeywordExtractionGateway,
    private val experimentPostKeywordsGateway: ExperimentPostKeywordsLogGateway,
    private val memberGateway: MemberGateway,
    private val idGenerator: IdGenerator
) : UseCase<ExtractExperimentPostKeywordsUseCase.Input, ExtractExperimentPostKeywordsUseCase.Output> {

    data class Input(
        val memberId: String,
        val text: String
    )

    data class Output(
        val experimentPostKeyword: ExperimentPostKeyword
    )

    override fun execute(input: Input): Output {
        val member = memberGateway.getById(input.memberId)
        val experimentPostKeyword = experimentKeywordExtractionGateway.extractKeywords(input.text)
        val log = ExperimentPostKeywordsLog.newExperimentPostKeywordsLog(
            id = idGenerator.generateId(),
            member = member,
            response = experimentPostKeyword,
        )

        experimentPostKeywordsGateway.save(log)
        return Output(experimentPostKeyword)
    }
}
