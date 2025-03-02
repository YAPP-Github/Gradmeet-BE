package com.dobby.backend.application.usecase.quartz

import com.dobby.gateway.SchedulerTriggerGateway
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class TriggerSchedulerUseCaseTest : BehaviorSpec({

    // Mock 객체 생성
    val schedulerTriggerGateway = mockk<SchedulerTriggerGateway>(relaxed = true)

    // UseCase 생성
    val triggerSchedulerUseCase = TriggerSchedulerUseCase(schedulerTriggerGateway)

    beforeTest {
        clearMocks(schedulerTriggerGateway)
    }

    given("스케줄러 트리거 실행 요청이 들어왔을 때") {

        `when`("유효한 jobName과 jobGroup을 전달하면") {
            val input = TriggerSchedulerUseCase.Input(jobName = "testJob", jobGroup = "testGroup")

            every { schedulerTriggerGateway.triggerJob(input.jobName, input.jobGroup) } just Runs  // Void method

            then("스케줄러 트리거가 정상적으로 실행되어야 한다") {
                val output = triggerSchedulerUseCase.execute(input)

                output shouldBe TriggerSchedulerUseCase.Output
                verify(exactly = 1) { schedulerTriggerGateway.triggerJob(input.jobName, input.jobGroup) }
            }
        }

        `when`("jobGroup을 지정하지 않고 jobName만 전달하면") {
            val input = TriggerSchedulerUseCase.Input(jobName = "testJob")

            every { schedulerTriggerGateway.triggerJob(input.jobName, "DEFAULT") } just Runs

            then("기본 jobGroup인 'DEFAULT'로 실행되어야 한다") {
                val output = triggerSchedulerUseCase.execute(input)

                output shouldBe TriggerSchedulerUseCase.Output
                verify(exactly = 1) { schedulerTriggerGateway.triggerJob(input.jobName, "DEFAULT") }
            }
        }
    }
})
