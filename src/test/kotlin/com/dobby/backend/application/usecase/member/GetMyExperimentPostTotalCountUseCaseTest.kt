package com.dobby.backend.application.usecase.member

import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetMyExperimentPostTotalCountUseCaseTest : BehaviorSpec({
    val experimentPostGateway = mockk<ExperimentPostGateway>()
    val useCase = GetMyExperimentPostTotalCountUseCase(experimentPostGateway)

    given("유효한 memberId가 주어졌을 때") {
        val memberId = 1L
        val totalCount = 5

        every { experimentPostGateway.countExperimentPostsByMemberId(memberId) } returns totalCount

        `when`("useCase의 execute가 호출되면") {
            val input = GetMyExperimentPostTotalCountUseCase.Input(memberId)
            val result = useCase.execute(input)

            then("정상적으로 totalPostCount가 반환된다") {
                result.totalPostCount shouldBe totalCount
            }
        }
    }

    given("존재하지 않는 memberId가 주어졌을 때") {
        val memberId = 999L
        val totalCount = 0

        every { experimentPostGateway.countExperimentPostsByMemberId(memberId) } returns totalCount

        `when`("useCase의 execute가 호출되면") {
            val input = GetMyExperimentPostTotalCountUseCase.Input(memberId)
            val result = useCase.execute(input)

            then("totalPostCount가 0으로 반환된다") {
                result.totalPostCount shouldBe totalCount
            }
        }
    }
})
