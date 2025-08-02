package com.dobby.usecase.experiment

import com.dobby.exception.ExperimentPostKeywordsDailyLimitExceededException
import com.dobby.gateway.OpenAiGateway
import com.dobby.gateway.UsageLimitGateway
import com.dobby.gateway.experiment.ExperimentPostKeywordsLogGateway
import com.dobby.gateway.member.MemberGateway
import com.dobby.model.experiment.ExperimentPostKeywordsLog
import com.dobby.model.experiment.keyword.ExperimentPostKeywords
import com.dobby.usecase.UseCase
import com.dobby.util.IdGenerator

class ExtractExperimentPostKeywordsUseCase(
    private val openAiGateway: OpenAiGateway,
    private val experimentPostKeywordsGateway: ExperimentPostKeywordsLogGateway,
    private val memberGateway: MemberGateway,
    private val usageLimitGateway: UsageLimitGateway,
    private val idGenerator: IdGenerator
) : UseCase<ExtractExperimentPostKeywordsUseCase.Input, ExtractExperimentPostKeywordsUseCase.Output> {

    companion object {
        private const val DAILY_USAGE_LIMIT = 2
    }

    data class Input(
        val memberId: String,
        val text: String
    )

    data class Output(
        val experimentPostKeywords: ExperimentPostKeywords
    )

    override fun execute(input: Input): Output {
        val member = memberGateway.getById(input.memberId)
        // validateDailyUsageLimit(input.memberId)

        val experimentPostKeyword = openAiGateway.extractKeywords(input.text)
        val log = ExperimentPostKeywordsLog.newExperimentPostKeywordsLog(
            id = idGenerator.generateId(),
            member = member,
            response = experimentPostKeyword
        )

        experimentPostKeywordsGateway.save(log)
        return Output(experimentPostKeyword)
    }

    private fun validateDailyUsageLimit(memberId: String) {
        val isAllowed = usageLimitGateway.incrementAndCheckLimit(memberId, DAILY_USAGE_LIMIT)
        if (!isAllowed) {
            throw ExperimentPostKeywordsDailyLimitExceededException
        }
    }
}
