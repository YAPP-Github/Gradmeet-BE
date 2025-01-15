package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway

class GetTotalMyExperimentPostCountUseCase(
    private val experimentPostGateway: ExperimentPostGateway
): UseCase<GetTotalMyExperimentPostCountUseCase.Input, GetTotalMyExperimentPostCountUseCase.Output> {

    data class Input(
        val memberId: Long
    )

    data class Output(
        val totalPostCount: Int
    )

    override fun execute(input: Input): Output {
        val totalPostCount = experimentPostGateway.countExperimentPostsByMemberId(input.memberId)
        return Output(totalPostCount = totalPostCount)
    }
}
