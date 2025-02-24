package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.enums.MatchType
import com.dobby.backend.domain.enums.experiment.RecruitStatus
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetExperimentPostTotalCountByCustomFilterUseCaseTest : BehaviorSpec({
    val experimentPostGateway = mockk<ExperimentPostGateway>()
    val getExperimentPostTotalCountByCustomFilterUseCase = GetExperimentPostTotalCountByCustomFilterUseCase(experimentPostGateway)

    given("유효한 필터 조건이 주어졌을 때") {
        val input = GetExperimentPostTotalCountByCustomFilterUseCase.Input(
            matchType = MatchType.ALL,
            studyTarget = null,
            locationTarget = null,
            recruitStatus = RecruitStatus.ALL
        )

        every {
            experimentPostGateway.countExperimentPostsByCustomFilter(any())
        } returns 10

        `when`("execute가 호출되면") {
            val result = getExperimentPostTotalCountByCustomFilterUseCase.execute(input)

            then("올바른 게시글 개수를 반환한다") {
                result shouldBe 10
            }
        }
    }

    given("필터 조건에 맞는 게시글이 없을 때") {
        val input = GetExperimentPostTotalCountByCustomFilterUseCase.Input(
            matchType = MatchType.OFFLINE,
            studyTarget = null,
            locationTarget = null,
            recruitStatus = RecruitStatus.OPEN
        )

        every {
            experimentPostGateway.countExperimentPostsByCustomFilter(any())
        } returns 0

        `when`("execute가 호출되면") {
            val result = getExperimentPostTotalCountByCustomFilterUseCase.execute(input)

            then("게시글 개수가 0으로 반환된다") {
                result shouldBe 0
            }
        }
    }
})
