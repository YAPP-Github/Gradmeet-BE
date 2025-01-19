package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import com.dobby.backend.infrastructure.database.entity.enums.experiment.RecruitStatus

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
        val regionDataMap = regionData.associateBy { it.get(0, Region::class.java) }

        val area = allRegions.map { region ->
            val areaName = region.toString()
            val count = regionDataMap[region]?.get(1, Long::class.java)?.toInt() ?: 0

            PostCountsByRegion(name = areaName, count = count)
        }

        return Output(total, area)
    }
}
