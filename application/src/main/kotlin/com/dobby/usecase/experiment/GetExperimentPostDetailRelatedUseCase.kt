package com.dobby.usecase.experiment

import com.dobby.enums.experiment.TimeSlot
import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.usecase.UseCase
import java.time.LocalDate

class GetExperimentPostDetailRelatedUseCase(
    private val experimentPostGateway: ExperimentPostGateway
) : UseCase<GetExperimentPostDetailRelatedUseCase.Input, GetExperimentPostDetailRelatedUseCase.Output> {

    data class Input(
        val experimentPostId: String
    )

    data class Output(
        val postInfo: List<PostInfoOutput>
    )

    data class PostInfoOutput(
        val experimentPostId: String,
        val title: String,
        val views: Int,
        val count: Int?,
        val timeRequired: TimeSlot?,
        val isOnCampus: Boolean,
        val place: String?,
        val reward: String,
        val recruitStatus: Boolean,
        val durationInfo: DurationInfoOutput
    )

    data class DurationInfoOutput(
        val startDate: LocalDate?,
        val endDate: LocalDate?
    )

    override fun execute(input: Input): Output {
        val next = experimentPostGateway.findNextPost(input.experimentPostId)
        val prevLimit = if (next == null) 2 else 1
        val prevPosts = experimentPostGateway.findPrevPosts(input.experimentPostId, prevLimit)

        val posts = buildList {
            next?.let { add(it) }
            addAll(prevPosts)
        }

        return Output(
            postInfo = posts.map { post ->
                PostInfoOutput(
                    experimentPostId = post.id,
                    title = post.title,
                    views = post.views,
                    count = post.count,
                    timeRequired = post.timeRequired,
                    isOnCampus = post.isOnCampus,
                    place = post.place,
                    reward = post.reward,
                    recruitStatus = post.recruitStatus,
                    durationInfo = DurationInfoOutput(
                        startDate = post.startDate,
                        endDate = post.endDate
                    )
                )
            }
        )
    }
}
