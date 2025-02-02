package com.dobby.backend.application.usecase.member.email

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.ExperimentPost

class GetMatchingExperimentPostsUseCase(
    private val experimentPostGateway: ExperimentPostGateway
): UseCase<GetMatchingExperimentPostsUseCase.Input, GetMatchingExperimentPostsUseCase.Output> {

    data class Input(
        val requestTime: String
    )

    data class Output(
        val matchingPosts: Map<String, List<ExperimentPost>?>
    )

    override fun execute(input : Input): Output {
        val result = experimentPostGateway.findMatchingExperimentPosts()
        return Output(matchingPosts = result)
    }
}
