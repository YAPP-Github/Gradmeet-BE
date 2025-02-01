package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.ExperimentPostNotFoundException
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway

class DeleteExperimentPostUseCase(
    private val experimentPostGateway: ExperimentPostGateway
): UseCase<DeleteExperimentPostUseCase.Input, Unit> {
    data class Input(
        val memberId: String,
        val postId: String
    )

    override fun execute(input: Input) {
        val post = experimentPostGateway.findExperimentPostByMemberIdAndPostId(
            memberId = input.memberId,
            postId = input.postId
        ) ?: throw ExperimentPostNotFoundException

        experimentPostGateway.delete(post)
    }
}
