package com.dobby.usecase.experiment

import com.dobby.usecase.UseCase
import com.dobby.exception.ExperimentPostNotFoundException
import com.dobby.exception.ExperimentPostRecruitStatusException
import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.model.experiment.ExperimentPost
import java.time.LocalDateTime

class UpdateExperimentPostRecruitStatusUseCase(
    private val experimentPostGateway: ExperimentPostGateway
): UseCase<UpdateExperimentPostRecruitStatusUseCase.Input, UpdateExperimentPostRecruitStatusUseCase.Output> {

    data class Input(
        val memberId: String,
        val postId: String
    )

    data class Output(
        val experimentPost: ExperimentPost
    )

    override fun execute(input: Input): Output {
        val post = experimentPostGateway.findExperimentPostByMemberIdAndPostId(
            memberId = input.memberId,
            postId = input.postId
        ) ?: throw ExperimentPostNotFoundException

        validateRecruitStatus(post)
        val updatedPost = experimentPostGateway.save(
            post.updateRecruitStatus(
                recruitStatus = false,
                updatedAt = LocalDateTime.now()
            )
        )

        return Output(
            experimentPost = updatedPost
        )
    }

    private fun validateRecruitStatus(post: ExperimentPost) {
        if (!post.recruitStatus) {
            throw ExperimentPostRecruitStatusException
        }
    }
}
