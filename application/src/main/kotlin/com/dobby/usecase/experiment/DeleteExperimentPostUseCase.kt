package com.dobby.usecase.experiment

import com.dobby.usecase.UseCase
import com.dobby.exception.ExperimentPostNotFoundException
import com.dobby.gateway.experiment.ExperimentPostGateway

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
