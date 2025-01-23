package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate

class UpdateExperimentPostUseCaseTest : BehaviorSpec({

    val experimentPostGateway = mockk<ExperimentPostGateway>()
    val updateExpiredExperimentPostUseCase = UpdateExpiredExperimentPostUseCase(experimentPostGateway)

    given("게시글이 만료되어 상태가 업데이트되는 경우") {
        val currentDate = LocalDate.of(2025, 1, 16)
        val input = UpdateExpiredExperimentPostUseCase.Input(currentDate)
        every { experimentPostGateway.updateExperimentPostStatus(any()) } returns 5L

        `when`("Execute를 호출하면") {
            then("UseCase는 업데이트된 게시글 수를 반환해야 한다") {
                val output = updateExpiredExperimentPostUseCase.execute(input)
                output.affectedRowsCount shouldBe 5L
                verify(exactly = 1) { experimentPostGateway.updateExperimentPostStatus(currentDate) }
            }
        }
    }

    given("업데이트할 게시글이 없는 경우") {
        val currentDate = LocalDate.of(2025, 1, 17)
        val input = UpdateExpiredExperimentPostUseCase.Input(currentDate)

        // Mock 설정을 각 테스트에서 바로 적용하지 않고 초기화 시 설정
        every { experimentPostGateway.updateExperimentPostStatus(currentDate) } returns 0L

        `when`("Execute를 호출하면") {
            then("UseCase는 0을 반환해야 한다") {
                val output = updateExpiredExperimentPostUseCase.execute(input)
                output.affectedRowsCount shouldBe 0L
                verify(exactly = 1) { experimentPostGateway.updateExperimentPostStatus(currentDate) }
            }
        }
    }

    given("Gateway에서 예외를 던지면") {
        val currentDate = LocalDate.of(2025, 1, 18)
        val input = UpdateExpiredExperimentPostUseCase.Input(currentDate)
        val exceptionMessage = "Database error"

        // 예외를 던지는 Mock 설정
        every { experimentPostGateway.updateExperimentPostStatus(currentDate) } throws RuntimeException(exceptionMessage)

        `when`("Execute를 호출하면") {
            then("UseCase는 예외를 상위로 전달해야 한다") {
                val exception = shouldThrow<RuntimeException> {
                    updateExpiredExperimentPostUseCase.execute(input)
                }

                exception.message shouldBe exceptionMessage
                verify(exactly = 1) { experimentPostGateway.updateExperimentPostStatus(currentDate) }
            }
        }
    }
})
