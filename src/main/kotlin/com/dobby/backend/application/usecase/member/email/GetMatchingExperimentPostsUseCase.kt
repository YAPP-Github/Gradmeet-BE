package com.dobby.backend.application.usecase.member.email

import com.dobby.backend.application.usecase.UseCase
import com.dobby.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.domain.model.experiment.ExperimentPost
import java.time.LocalDateTime

class GetMatchingExperimentPostsUseCase(
    private val experimentPostGateway: ExperimentPostGateway
): UseCase<GetMatchingExperimentPostsUseCase.Input, GetMatchingExperimentPostsUseCase.Output> {

    data class Input(
        val requestTime: LocalDateTime
    )

    data class Output(
        val matchingPosts: Map<String, List<ExperimentPost>>
    )

    override fun execute(input : Input): Output {
        val result = experimentPostGateway.findMatchingExperimentPosts()
        return Output(matchingPosts = result)
    }
}
