package com.dobby.backend.application.mapper

import com.dobby.backend.application.usecase.experiment.GetExperimentPostTotalCountByCustomFilterUseCase
import com.dobby.backend.application.usecase.experiment.GetExperimentPostsUseCase
import com.dobby.backend.application.usecase.experiment.UpdateExperimentPostUseCase
import com.dobby.backend.domain.model.experiment.*
import java.time.LocalDateTime

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

    fun toDomain(input: UpdateExperimentPostUseCase.Input, existingPost: ExperimentPost): ExperimentPost {
        return existingPost.copy(
            title = input.title ?: existingPost.title,
            reward = input.reward ?: existingPost.reward,
            startDate = input.startDate ?: existingPost.startDate,
            endDate = input.endDate ?: existingPost.endDate,
            content = input.content ?: existingPost.content,
            count = input.count ?: existingPost.count,
            leadResearcher = input.leadResearcher ?: existingPost.leadResearcher,
            detailedAddress = input.detailedAddress ?: existingPost.detailedAddress,
            matchType = input.matchType ?: existingPost.matchType,
            univName = input.univName ?: existingPost.univName,
            region = input.region ?: existingPost.region,
            area = input.area ?: existingPost.area,
            images = input.imageListInfo?.images?.map { imageUrl ->
                val existingImage = existingPost.images.find { it.imageUrl == imageUrl }
                ExperimentImage(
                    id = existingImage?.id ?: 0L,
                    experimentPost = existingPost,
                    imageUrl = imageUrl
                )
            }?.toMutableList() ?: existingPost.images,
            updatedAt = LocalDateTime.now()
        ).apply {
            input.targetGroupInfo?.let {
                this.targetGroup.update(
                    startAge = it.startAge,
                    endAge = it.endAge,
                    genderType = it.genderType,
                    otherCondition = it.otherCondition
                )
            }
            input.applyMethodInfo?.let {
                this.applyMethod.update(
                    content = it.content,
                    formUrl = it.formUrl,
                    phoneNum = it.phoneNum
                )
            }
        }
    }
}
