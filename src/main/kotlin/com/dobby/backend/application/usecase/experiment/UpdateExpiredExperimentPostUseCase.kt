package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.gateway.experiment.ExperimentPostGateway
import java.time.LocalDate

class UpdateExpiredExperimentPostUseCase(
    private val experimentPostGateway: ExperimentPostGateway
) : UseCase<UpdateExpiredExperimentPostUseCase.Input, UpdateExpiredExperimentPostUseCase.Output> {
    data class Input(
        val currentDate : LocalDate
    )
    data class Output(
        val affectedRowsCount: Long
    )

    override fun execute(input: Input): Output {
        return Output(
            experimentPostGateway.updateExperimentPostStatus(input.currentDate)
        )
    }
}
