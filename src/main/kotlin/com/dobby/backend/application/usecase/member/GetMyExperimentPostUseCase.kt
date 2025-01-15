package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.mapper.MemberMapper
import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import java.time.LocalDate

class GetMyExperimentPostUseCase(
    private val experimentPostGateway: ExperimentPostGateway
): UseCase<GetMyExperimentPostUseCase.Input, List<GetMyExperimentPostUseCase.Output>> {

    data class Input(
        val memberId: Long,
        val pagination: PaginationInput
    )

    data class PaginationInput(
        val page: Int = 1,
        val count: Int = 6,
        val order: String = "DESC"
    )

    data class Output(
        val experimentPostId: Long,
        val title: String,
        val content: String,
        val views: Int,
        val recruitDone: Boolean,
        val uploadDate: LocalDate
    )

    override fun execute(input: Input): List<Output> {
        val posts = experimentPostGateway.findExperimentPostsByMemberIdWithPagination(
            memberId = input.memberId,
            pagination = MemberMapper.toDomainPagination(input.pagination),
            order = input.pagination.order
        )

        return posts?.map { post ->
            Output(
                experimentPostId = post.id,
                title = post.title,
                content = post.content,
                views = post.views,
                recruitDone = post.recruitDone,
                uploadDate = post.createdAt.toLocalDate()
            )
        } ?: emptyList()
    }
}
