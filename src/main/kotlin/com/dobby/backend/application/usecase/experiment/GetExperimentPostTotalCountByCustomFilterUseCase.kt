package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.mapper.ExperimentMapper
import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.experiment.RecruitStatus

class GetExperimentPostTotalCountByCustomFilterUseCase(
    private val experimentPostGateway: ExperimentPostGateway
) : UseCase<GetExperimentPostTotalCountByCustomFilterUseCase.Input, Int> {

    data class Input(
        val matchType: MatchType?,
        val studyTarget: GetExperimentPostsUseCase.StudyTargetInput?,
        val locationTarget: GetExperimentPostsUseCase.LocationTargetInput?,
        val recruitStatus: RecruitStatus
    )

    override fun execute(input: Input): Int {
        return experimentPostGateway.countExperimentPostsByCustomFilter(
            ExperimentMapper.toDomainFilter(input)
        )
    }
}
