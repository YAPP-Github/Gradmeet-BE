package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.domain.exception.ExperimentPostNotFoundException
import com.dobby.domain.exception.ExperimentPostRecruitStatusException
import com.dobby.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.domain.model.experiment.ExperimentPost
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
