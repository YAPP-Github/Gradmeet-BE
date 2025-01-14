package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.infrastructure.database.entity.enums.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import java.time.LocalDate

class GetExperimentPostsUseCase (
    private val experimentPostGateway: ExperimentPostGateway
): UseCase<GetExperimentPostsUseCase.Input, List<GetExperimentPostsUseCase.Output>>{
    data class Input (
        val customFilter: CustomFilter,
        val pagination: Pagination
    ) {
        fun toDomainFilter(): com.dobby.backend.domain.model.experiment.CustomFilter {
            return com.dobby.backend.domain.model.experiment.CustomFilter(
                matchType = customFilter.matchType,
                studyTarget = this.customFilter.studyTarget?.let {
                    com.dobby.backend.domain.model.experiment.StudyTarget(
                        gender = it.gender,
                        age = it.age
                    )
                },
                locationTarget = this.customFilter.locationTarget?.let {
                    com.dobby.backend.domain.model.experiment.LocationTarget(
                        region = it.region,
                        areas = it.areas
                    )
                },
                recruitDone = this.customFilter.recruitDone
            )
        }

        fun toDomainPagination(): com.dobby.backend.domain.model.experiment.Pagination {
            return com.dobby.backend.domain.model.experiment.Pagination(
                page = this.pagination.page,
                count = this.pagination.count
            )
        }
    }

    data class CustomFilter(
        val matchType : MatchType?,
        val studyTarget: StudyTarget?,
        val locationTarget: LocationTarget?,
        val recruitDone: Boolean?,
    )

    data class StudyTarget (
        val gender : GenderType?,
        val age: Int?
    )

    data class LocationTarget (
        val region: Region?,
        val areas: List<Area>?,
    )

    data class Pagination (
        val page: Int = 1,
        val count: Int = 6,
    )

    data class Output(
        val postInfo: PostInfo
    )

    data class PostInfo(
        val postId: Long,
        val title: String,
        val views: Int,
        val univName: String,
        val reward: String,
        val recruitDone: Boolean,
        val durationInfo: DurationInfo
    )

    data class DurationInfo(
        val startDate: LocalDate?,
        val endDate: LocalDate?
    )

    override fun execute(input: Input) : List<Output> {
        val posts = experimentPostGateway.findExperimentPostsByCustomFilter(
            input.toDomainFilter(),
            input.toDomainPagination()
        )

        return posts?.map { post ->
            Output(
                postInfo = PostInfo(
                    postId = post.id,
                    title = post.title,
                    views = post.views,
                    univName = post.univName,
                    reward = post.reward,
                    recruitDone = post.recruitDone,
                    durationInfo = DurationInfo(
                        startDate = post.startDate,
                        endDate = post.endDate
                    )
                )
            )
        } ?: emptyList()
    }

}
