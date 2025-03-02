package com.dobby.usecase.experiment

import com.dobby.usecase.UseCase
import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.model.experiment.CustomFilter
import com.dobby.model.experiment.LocationTarget
import com.dobby.model.experiment.StudyTarget
import com.dobby.enums.MatchType
import com.dobby.enums.experiment.RecruitStatus

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
