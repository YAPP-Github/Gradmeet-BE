package com.dobby.usecase.experiment

import com.dobby.exception.ExperimentPostKeywordsDailyLimitExceededException
import com.dobby.gateway.experiment.ExperimentKeywordExtractionGateway
import com.dobby.gateway.experiment.ExperimentPostKeywordsLogGateway
import com.dobby.gateway.member.MemberGateway
import com.dobby.model.experiment.ExperimentPostKeywordsLog
import com.dobby.model.experiment.keyword.ExperimentPostKeyword
import com.dobby.usecase.UseCase
import com.dobby.util.IdGenerator
import com.dobby.util.TimeProvider

class ExtractExperimentPostKeywordsUseCase(
    private val experimentKeywordExtractionGateway: ExperimentKeywordExtractionGateway,
    private val experimentPostKeywordsGateway: ExperimentPostKeywordsLogGateway,
    private val memberGateway: MemberGateway,
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
        val experimentPostKeyword: ExperimentPostKeyword
    )

    override fun execute(input: Input): Output {
        val member = memberGateway.getById(input.memberId)
        validateDailyUsageLimit(input.memberId)

        val experimentPostKeyword = experimentKeywordExtractionGateway.extractKeywords(input.text)
        val log = ExperimentPostKeywordsLog.newExperimentPostKeywordsLog(
            id = idGenerator.generateId(),
            member = member,
            response = experimentPostKeyword
        )

        experimentPostKeywordsGateway.save(log)
        return Output(experimentPostKeyword)
    }

    private fun validateDailyUsageLimit(memberId: String) {
        val today = TimeProvider.currentDateTime().toLocalDate()
        val startOfDay = today.atStartOfDay()
        val endOfDay = today.plusDays(1).atStartOfDay()

        val todayUsageCount = experimentPostKeywordsGateway.countByMemberIdAndCreatedAtBetween(
            memberId = memberId,
            start = startOfDay,
            end = endOfDay
        )

        if (todayUsageCount >= DAILY_USAGE_LIMIT) {
            throw ExperimentPostKeywordsDailyLimitExceededException
        }
    }
}
