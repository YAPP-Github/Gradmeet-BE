package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.ExperimentPostNotFoundException
import com.dobby.backend.domain.exception.ExperimentPostRecruitStatusException
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.ExperimentPost
import java.time.LocalDateTime

class UpdateExperimentPostRecruitStatusUseCase(
    private val experimentPostGateway: ExperimentPostGateway
): UseCase<UpdateExperimentPostRecruitStatusUseCase.Input, UpdateExperimentPostRecruitStatusUseCase.Output> {

    data class Input(
        val memberId: Long,
        val postId: Long
    )

    data class Output(
        val experimentPost: ExperimentPost
    )

    override fun execute(input: Input): Output {
        val post = experimentPostGateway.findExperimentPostByMemberIdAndPostId(
            memberId = input.memberId,
            postId = input.postId
        ) ?: throw ExperimentPostNotFoundException()

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
            throw ExperimentPostRecruitStatusException()
        }
    }
}
