package com.dobby.backend.application.usecase.experiment//package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.*
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.infrastructure.database.entity.enums.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area.Companion.isAll
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import java.time.LocalDate

class GetExperimentPostsUseCase (
    private val experimentPostGateway: ExperimentPostGateway
): UseCase<GetExperimentPostsUseCase.Input, List<GetExperimentPostsUseCase.Output>>{
    data class Input (
        @Schema(description = "커스텀 필터")
        val customFilter: CustomFilter,

        @Schema(description = "페이지네이션")
        val pagination: Pagination
    ) {
        fun toDomainFilter(): com.dobby.backend.domain.model.experiment.CustomFilter {
            return com.dobby.backend.domain.model.experiment.CustomFilter(
                method = customFilter.method,
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
        @Schema(description = "진행 방식 필터")
        val method : MatchType?,

        @Schema(description = "모집 대상")
        val studyTarget: StudyTarget?,

        @Schema(description = "지역")
        val locationTarget: LocationTarget?,

        @Schema(description = "공고 상태 필터")
        val recruitDone: Boolean?,
    )

    data class StudyTarget (
        @Schema(description = "모집 대상 - 성별 필터")
        val gender : GenderType?,

        @Schema(description = "모집 대상 - 나이 필터")
        val age: Int?
    )

    data class LocationTarget (
        @Schema(description = "지역 - 시/도 필터")
        val region: Region?,

        @Schema(description = "지역 - 시/군/구 필터")
        val areas: List<Area>?,
    )

    data class Pagination (
        @field:Min(1)
        @Schema(description = "페이지네이션 - 기본값: 1페이지")
        val page: Int = 1,

        @field:Min(1)
        @Schema(description = "한 페이지 당 공고 수 - 기본값: 6개")
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
        validateFilter(input)
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

   public fun validateFilter(input: Input) {
        val locationInfo = input.customFilter.locationTarget

        if (locationInfo?.areas != null) {
            val isAll = locationInfo.areas.any { it.isAll() }

            if (isAll && locationInfo.areas.size != 1)
                throw ExperimentAreaSelectionException()

            if (locationInfo.areas.size > 5)
                throw ExperimentAreaOverflowException()

            val selectedRegion = locationInfo.region
            if (selectedRegion != null) {
                val validAreas = Area.findByRegion(selectedRegion).map { it.name }
                if (locationInfo.areas.map { it.name }.any { it !in validAreas }) {
                    throw ExperimentAreaInCorrectException()
                }
            }
        }
    }

}
