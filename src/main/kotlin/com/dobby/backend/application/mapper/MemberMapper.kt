package com.dobby.backend.application.mapper

import com.dobby.backend.application.usecase.member.GetMyExperimentPostUseCase
import com.dobby.backend.domain.model.experiment.Pagination

object MemberMapper {
    fun toDomainPagination(pagination: GetMyExperimentPostUseCase.PaginationInput): Pagination {
        return Pagination(
            page = pagination.page,
            count = pagination.count
        )
    }
}