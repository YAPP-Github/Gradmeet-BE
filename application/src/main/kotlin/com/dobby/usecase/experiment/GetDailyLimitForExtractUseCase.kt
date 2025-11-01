package com.dobby.usecase.experiment

import com.dobby.gateway.UsageLimitGateway
import com.dobby.usecase.UseCase
import java.time.LocalDateTime

class GetDailyLimitForExtractUseCase(
    private val usageLimitGateway: UsageLimitGateway
) : UseCase<GetDailyLimitForExtractUseCase.Input, GetDailyLimitForExtractUseCase.Output> {

    companion object {
        private const val DAILY_USAGE_LIMIT = 2
    }

    data class Input(
        val memberId: String
    )

    data class Output(
        val count: Long,
        val limit: Int,
        val remainingCount: Long,
        val resetsAt: LocalDateTime
    )

    override fun execute(input: Input): Output {
        val snapshot = usageLimitGateway.getCurrentUsage(
            memberId = input.memberId,
            dailyLimit = DAILY_USAGE_LIMIT
        )

        return Output(
            count = snapshot.count,
            limit = snapshot.limit,
            remainingCount = snapshot.remainingCount,
            resetsAt = snapshot.resetsAt
        )
    }
}
