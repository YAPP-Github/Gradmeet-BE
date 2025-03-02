package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.model.Pagination
import com.dobby.backend.application.usecase.UseCase
import com.dobby.domain.gateway.experiment.ExperimentPostGateway
import java.time.LocalDate

class GetMyExperimentPostsUseCase(
    private val experimentPostGateway: ExperimentPostGateway
): UseCase<GetMyExperimentPostsUseCase.Input, List<GetMyExperimentPostsUseCase.Output>> {

    data class Input(
        val memberId: String,
        val pagination: PaginationInput
    )

    data class PaginationInput(
        val page: Int = 1,
        val count: Int = 6,
        val order: String = "DESC"
    )

    data class Output(
        val experimentPostId: String,
        val title: String,
        val content: String,
        val views: Int,
        val recruitStatus: Boolean,
        val uploadDate: LocalDate
    )

    override fun execute(input: Input): List<Output> {
        val posts = experimentPostGateway.findExperimentPostsByMemberIdWithPagination(
            memberId = input.memberId,
            page = input.pagination.page,
            count = input.pagination.count,
            order = input.pagination.order
        )

        return posts?.map { post ->
            Output(
                experimentPostId = post.id,
                title = post.title,
                content = post.content,
                views = post.views,
                recruitStatus = post.recruitStatus,
                uploadDate = post.createdAt.toLocalDate()
            )
        } ?: emptyList()
    }
}
