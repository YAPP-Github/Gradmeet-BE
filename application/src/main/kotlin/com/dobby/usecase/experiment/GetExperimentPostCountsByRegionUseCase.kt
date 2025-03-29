package com.dobby.usecase.experiment

import com.dobby.enums.areaInfo.Region
import com.dobby.enums.experiment.RecruitStatus
import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.usecase.UseCase

class GetExperimentPostCountsByRegionUseCase(
    private val experimentPostGateway: ExperimentPostGateway
) : UseCase<GetExperimentPostCountsByRegionUseCase.Input, GetExperimentPostCountsByRegionUseCase.Output> {

    data class Input(
        val region: String?,
        val recruitStatus: RecruitStatus
    )

    data class Output(
        val total: Int,
        val area: List<PostCountsByRegion>
    )

    data class PostCountsByRegion(
        val name: String,
        val count: Int
    )

    override fun execute(input: Input): Output {
        val total = when (input.recruitStatus) {
            RecruitStatus.ALL -> experimentPostGateway.countExperimentPosts()
            RecruitStatus.OPEN -> experimentPostGateway.countExperimentPostsByRecruitStatus(true)
        }

        val allRegions = Region.values().filter { it != Region.NONE }
        val regionData = when (input.recruitStatus) {
            RecruitStatus.ALL -> experimentPostGateway.countExperimentPostGroupedByRegion()
            RecruitStatus.OPEN -> experimentPostGateway.countExperimentPostsByRecruitStatusGroupedByRegion(true)
        }
        val regionDataMap = regionData.associateBy { it.region }

        val area = allRegions.map { region ->
            val areaName = region.toString()
            val count = regionDataMap[region]?.count?.toInt() ?: 0

            PostCountsByRegion(name = areaName, count = count)
        }

        return Output(total, area)
    }
}
