package com.dobby.backend.application.usecase.experiment

import com.dobby.exception.ExperimentPostNotFoundException
import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.model.experiment.ExperimentPost
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class DeleteExperimentPostUseCaseTest : BehaviorSpec({

    val experimentPostGateway = mockk<ExperimentPostGateway>()
    val deleteExperimentPostUseCase = DeleteExperimentPostUseCase(experimentPostGateway)

    given("게시글을 삭제할 때") {
        val memberId = "1"
        val postId = "2"
        val input = DeleteExperimentPostUseCase.Input(memberId, postId)

        val existingPost = mockk<ExperimentPost>()
        every { experimentPostGateway.findExperimentPostByMemberIdAndPostId(memberId, postId) } returns existingPost

        `when`("게시글이 존재하면") {
            every { experimentPostGateway.delete(existingPost) } returns Unit

            then("게시글을 삭제하고 아무 값도 반환하지 않아야 한다") {
                deleteExperimentPostUseCase.execute(input)
                verify(exactly = 1) { experimentPostGateway.findExperimentPostByMemberIdAndPostId(memberId, postId) }
                verify(exactly = 1) { experimentPostGateway.delete(existingPost) }
            }
        }
    }

    given("게시글을 삭제하려는데 게시글이 존재하지 않는 경우") {
        val memberId = "1"
        val postId = "999"
        val input = DeleteExperimentPostUseCase.Input(memberId, postId)

        every { experimentPostGateway.findExperimentPostByMemberIdAndPostId(memberId, postId) } returns null

        `when`("게시글을 찾지 못하면") {
            then("ExperimentPostNotFoundException 예외가 발생해야 한다") {
                shouldThrow<ExperimentPostNotFoundException> {
                    deleteExperimentPostUseCase.execute(input)
                }
                verify(exactly = 1) { experimentPostGateway.findExperimentPostByMemberIdAndPostId(memberId, postId) }
            }
        }
    }
})
