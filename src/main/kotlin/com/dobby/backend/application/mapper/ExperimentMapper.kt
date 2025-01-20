package com.dobby.backend.application.mapper

import com.dobby.backend.application.usecase.experiment.GetExperimentPostTotalCountByCustomFilterUseCase
import com.dobby.backend.application.usecase.experiment.GetExperimentPostsUseCase
import com.dobby.backend.domain.model.experiment.CustomFilter
import com.dobby.backend.domain.model.experiment.LocationTarget
import com.dobby.backend.domain.model.experiment.Pagination
import com.dobby.backend.domain.model.experiment.StudyTarget

object ExperimentMapper {
    fun toDomainFilter(customFilter: GetExperimentPostsUseCase.CustomFilterInput): CustomFilter {
        return CustomFilter(
            matchType = customFilter.matchType,
            studyTarget = customFilter.studyTarget?.let {
                StudyTarget(
                    gender = it.gender,
                    age = it.age
                )
            },
            locationTarget = customFilter.locationTarget?.let {
                LocationTarget(
                    region = it.region,
                    areas = it.areas
                )
            },
            recruitStatus = customFilter.recruitStatus
        )
    }

    fun toDomainFilter(customFilter: GetExperimentPostTotalCountByCustomFilterUseCase.Input): CustomFilter {
        return CustomFilter(
            matchType = customFilter.matchType,
            studyTarget = customFilter.studyTarget?.let {
                StudyTarget(
                    gender = it.gender,
                    age = it.age
                )
            },
            locationTarget = customFilter.locationTarget?.let {
                LocationTarget(
                    region = it.region,
                    areas = it.areas
                )
            },
            recruitStatus = customFilter.recruitStatus
        )
    }

    fun toDomainPagination(pagination: GetExperimentPostsUseCase.PaginationInput): Pagination {
        return Pagination(
            page = pagination.page,
            count = pagination.count
        )
    }
}
