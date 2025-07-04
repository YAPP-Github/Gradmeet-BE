package com.dobby.usecase.experiment

import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import com.dobby.enums.experiment.RecruitStatus
import com.dobby.exception.InvalidRequestValueException
import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.model.experiment.ExperimentPostStats
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetExperimentPostCountsByAreaUseCaseTest : BehaviorSpec({
    val experimentPostGateway = mockk<ExperimentPostGateway>()
    val getExperimentPostCountsByAreaUseCase = GetExperimentPostCountsByAreaUseCase(experimentPostGateway)

    given("유효한 지역 이름이 주어졌을 때") {
        val regionName = "SEOUL"
        val region = Region.fromDisplayName(regionName)

        val regionData = listOf(
            ExperimentPostStats(regionName = regionName, areaName = "GEUMCHEONGU", count = 10L),
            ExperimentPostStats(regionName = regionName, areaName = "GANGNAMGU", count = 5L)
        )

        every { experimentPostGateway.countExperimentPostsByRegion(region) } returns 15
        every { experimentPostGateway.countExperimentPostByRegionGroupedByArea(region) } returns regionData

        `when`("모든 공고를 조회하는 execute가 호출되면") {
            val input = GetExperimentPostCountsByAreaUseCase.Input(regionName, RecruitStatus.ALL)
            val result = getExperimentPostCountsByAreaUseCase.execute(input)

            then("총 공고 개수가 반환된다") {
                result.total shouldBe 15
            }

            then("지역별 공고 개수가 반환된다") {
                result.area.size shouldBe 26
                result.area.find { it.name == Area.SEOUL_ALL.displayName }?.count shouldBe 15
                result.area.find { it.name == Area.GEUMCHEONGU.displayName }?.count shouldBe 10
                result.area.find { it.name == Area.GANGNAMGU.displayName }?.count shouldBe 5
            }
        }

        every { experimentPostGateway.countExperimentPostsByRegionAndRecruitStatus(region, true) } returns 15
        every { experimentPostGateway.countExperimentPostByRegionAndRecruitStatusGroupedByArea(region, true) } returns regionData

        `when`("모집중인 공고를 조회하는 execute가 호출되면") {
            val input = GetExperimentPostCountsByAreaUseCase.Input(regionName, RecruitStatus.OPEN)
            val result = getExperimentPostCountsByAreaUseCase.execute(input)

            then("총 공고 개수가 반환된다") {
                result.total shouldBe 15
            }

            then("지역별 공고 개수가 반환된다") {
                result.area.size shouldBe 26
                result.area.find { it.name == Area.SEOUL_ALL.displayName }?.count shouldBe 15
                result.area.find { it.name == Area.GEUMCHEONGU.displayName }?.count shouldBe 10
                result.area.find { it.name == Area.GANGNAMGU.displayName }?.count shouldBe 5
            }
        }
    }

    given("잘못된 지역 이름이 주어졌을 때") {
        val invalidRegionName = "INVALID_REGION"

        `when`("execute가 호출되면") {
            val input = GetExperimentPostCountsByAreaUseCase.Input(invalidRegionName, RecruitStatus.ALL)

            then("예외가 발생한다") {
                val exception = runCatching { getExperimentPostCountsByAreaUseCase.execute(input) }.exceptionOrNull()
                exception shouldBe InvalidRequestValueException
            }
        }
    }
})
