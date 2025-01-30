package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.ExperimentPostNotFoundException
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway

class GetExperimentPostApplyMethodUseCase(
    private val experimentPostGateway: ExperimentPostGateway,
): UseCase<GetExperimentPostApplyMethodUseCase.Input, GetExperimentPostApplyMethodUseCase.Output> {

    data class Input(
        val experimentPostId: Long
    )

    data class Output(
        val applyMethodId: Long,
        val phoneNum: String?,
        val formUrl: String?,
        val content: String
    )

    override fun execute(input: Input): Output {
        val experimentPost = experimentPostGateway.findById(input.experimentPostId)
            ?: throw ExperimentPostNotFoundException

        return Output(
            applyMethodId = experimentPost.applyMethod.id,
            phoneNum = experimentPost.applyMethod.phoneNum,
            formUrl = experimentPost.applyMethod.formUrl,
            content = experimentPost.applyMethod.content
        )
    }
}
