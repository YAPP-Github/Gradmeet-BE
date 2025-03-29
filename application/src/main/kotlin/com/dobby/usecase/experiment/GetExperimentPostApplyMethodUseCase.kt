package com.dobby.usecase.experiment

import com.dobby.exception.ExperimentPostNotFoundException
import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.usecase.UseCase

class GetExperimentPostApplyMethodUseCase(
    private val experimentPostGateway: ExperimentPostGateway
) : UseCase<GetExperimentPostApplyMethodUseCase.Input, GetExperimentPostApplyMethodUseCase.Output> {

    data class Input(
        val experimentPostId: String
    )

    data class Output(
        val applyMethodId: String,
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
