package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.enums.areaInfo.Area
import com.dobby.backend.domain.enums.areaInfo.Region
import com.dobby.backend.domain.enums.experiment.RecruitStatus
import jakarta.persistence.Tuple

class GetExperimentPostCountsByAreaUseCase(
    private val experimentPostGateway: ExperimentPostGateway
) : UseCase<GetExperimentPostCountsByAreaUseCase.Input, GetExperimentPostCountsByAreaUseCase.Output> {

    data class Input(
        val region: String,
        val recruitStatus: RecruitStatus
    )

    data class Output(
        val total: Int,
        val area: List<PostCountsByArea>
    )

    data class PostCountsByArea(
        val name: String,
        val count: Int
    )

    override fun execute(input: Input): Output {
        val region = Region.fromDisplayName(input.region)

        val total = when (input.recruitStatus) {
            RecruitStatus.ALL -> experimentPostGateway.countExperimentPostsByRegion(region)
            RecruitStatus.OPEN -> experimentPostGateway.countExperimentPostsByRegionAndRecruitStatus(region, true)
        }

        val regionData = when (input.recruitStatus) {
            RecruitStatus.ALL -> region.let { experimentPostGateway.countExperimentPostByRegionGroupedByArea(it) }
            RecruitStatus.OPEN -> region.let { experimentPostGateway.countExperimentPostByRegionAndRecruitStatusGroupedByArea(it, true) }
        }
        val areaCounts = getAreaCounts(region, regionData)

        return Output(total, areaCounts)
    }

    private fun getAreaCounts(region: Region?, regionData: List<Tuple>): List<PostCountsByArea> {
        return region?.getAreas()?.map { area ->
            val areaCount = findAreaCount(regionData, area)
            PostCountsByArea(name = area.displayName, count = areaCount)
        } ?: emptyList()
    }

    private fun findAreaCount(regionData: List<Tuple>, area: Area): Int {
        return regionData.find { tuple ->
            val regionArea = tuple.get(0, Area::class.java)
            regionArea == area
        }?.get(1, Long::class.java)?.toInt() ?: 0
    }
}
