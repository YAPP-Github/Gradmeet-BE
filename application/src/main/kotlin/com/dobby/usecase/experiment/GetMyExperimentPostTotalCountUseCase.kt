package com.dobby.usecase.experiment

import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.usecase.UseCase

class GetMyExperimentPostTotalCountUseCase(
    private val experimentPostGateway: ExperimentPostGateway
) : UseCase<GetMyExperimentPostTotalCountUseCase.Input, GetMyExperimentPostTotalCountUseCase.Output> {

    data class Input(
        val memberId: String
    )

    data class Output(
        val totalPostCount: Int
    )

    override fun execute(input: Input): Output {
        val totalPostCount = experimentPostGateway.countExperimentPostsByMemberId(input.memberId)
        return Output(totalPostCount = totalPostCount)
    }
}
