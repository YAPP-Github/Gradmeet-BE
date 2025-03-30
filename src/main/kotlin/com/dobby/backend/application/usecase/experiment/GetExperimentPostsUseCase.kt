package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.model.Pagination
import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.CustomFilter
import com.dobby.backend.domain.model.experiment.LocationTarget
import com.dobby.backend.domain.model.experiment.StudyTarget
import com.dobby.backend.domain.enums.member.GenderType
import com.dobby.backend.domain.enums.MatchType
import com.dobby.backend.domain.enums.areaInfo.Area
import com.dobby.backend.domain.enums.areaInfo.Region
import com.dobby.backend.domain.enums.experiment.RecruitStatus
import java.time.LocalDate

class GetExperimentPostsUseCase (
    private val experimentPostGateway: ExperimentPostGateway
): UseCase<GetExperimentPostsUseCase.Input, List<GetExperimentPostsUseCase.Output>>{
    data class Input (
        val customFilter: CustomFilterInput,
        val pagination: PaginationInput
    )

    data class CustomFilterInput(
        val matchType : MatchType?,
        val studyTarget: StudyTargetInput?,
        val locationTarget: LocationTargetInput?,
        val recruitStatus: RecruitStatus,
    )

    data class StudyTargetInput(
        val gender : GenderType?,
        val age: Int?
    )

    data class LocationTargetInput(
        val region: Region?,
        val areas: List<Area>?,
    )

    data class PaginationInput(
        val page: Int = 1,
        val count: Int = 6,
        val order : String = "DESC"
    )

    data class Output(
        val postInfo: PostInfoOutput
    )

    data class PostInfoOutput(
        val experimentPostId: String,
        val title: String,
        val views: Int,
        val place: String?,
        val reward: String,
        val recruitStatus: Boolean,
        val durationInfo: DurationInfoOutput
    )

    data class DurationInfoOutput(
        val startDate: LocalDate?,
        val endDate: LocalDate?
    )

    override fun execute(input: Input) : List<Output> {
        val domainFilter = CustomFilter.newCustomFilter(
            input.customFilter.matchType,
            studyTarget = input.customFilter.studyTarget?.let { StudyTarget(it.gender, it.age) },
            locationTarget = input.customFilter.locationTarget?.let { LocationTarget(it.region, it.areas) },
            input.customFilter.recruitStatus
        )
        val pagination = Pagination(input.pagination.page, input.pagination.count)
        val posts = experimentPostGateway.findExperimentPostsByCustomFilter(
            domainFilter,
            pagination,
            input.pagination.order
        )

        return posts?.map { post ->
            Output(
                postInfo = PostInfoOutput(
                    experimentPostId = post.id,
                    title = post.title,
                    views = post.views,
                    place = post.place,
                    reward = post.reward,
                    recruitStatus = post.recruitStatus,
                    durationInfo = DurationInfoOutput(
                        startDate = post.startDate,
                        endDate = post.endDate
                    )
                )
            )
        } ?: emptyList()
    }

}
