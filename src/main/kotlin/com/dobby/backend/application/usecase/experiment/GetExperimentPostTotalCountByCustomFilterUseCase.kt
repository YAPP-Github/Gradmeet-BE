package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.domain.model.experiment.CustomFilter
import com.dobby.domain.model.experiment.LocationTarget
import com.dobby.domain.model.experiment.StudyTarget
import com.dobby.domain.enums.MatchType
import com.dobby.domain.enums.experiment.RecruitStatus

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
            CustomFilter.newCustomFilter(
                input.matchType,
                studyTarget = input.studyTarget?.let { StudyTarget(it.gender, it.age) },
                locationTarget = input.locationTarget?.let { LocationTarget(it.region, it.areas) },
                input.recruitStatus
            )
        )
    }
}
