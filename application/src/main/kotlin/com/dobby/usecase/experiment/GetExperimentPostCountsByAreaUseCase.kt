package com.dobby.usecase.experiment

import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import com.dobby.enums.experiment.RecruitStatus
import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.model.experiment.ExperimentPostStats
import com.dobby.usecase.UseCase

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

    private fun getAreaCounts(region: Region, regionData: List<ExperimentPostStats>): List<PostCountsByArea> {
        val subAreas = Area.values().filter {
            it.region == region && !it.displayName.endsWith("_ALL")
        }
        return region.getAreas().map { area ->
            val count = if (area.displayName.endsWith("_ALL")) {
                // "_ALL"이면 같은 region의 하위 area를 합산
                subAreas.sumOf { subArea -> findAreaCount(regionData, subArea) }
            } else {
                findAreaCount(regionData, area)
            }
            PostCountsByArea(name = area.displayName, count = count)
        }
    }

    private fun findAreaCount(regionData: List<ExperimentPostStats>, area: Area): Int {
        return regionData.find { stats ->
            stats.area == area
        }?.count?.toInt() ?: 0
    }
}
