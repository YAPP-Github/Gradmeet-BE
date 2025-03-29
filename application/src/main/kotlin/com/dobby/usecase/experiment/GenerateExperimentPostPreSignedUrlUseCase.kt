package com.dobby.usecase.experiment

import com.dobby.gateway.S3Gateway
import com.dobby.usecase.UseCase

class GenerateExperimentPostPreSignedUrlUseCase(
    private val s3Gateway: S3Gateway
) : UseCase<GenerateExperimentPostPreSignedUrlUseCase.Input, GenerateExperimentPostPreSignedUrlUseCase.Output> {
    data class Input(
        val fileName: String
    )

    data class Output(
        val preSignedUrl: String
    )

    override fun execute(input: Input): Output {
        return Output(s3Gateway.getExperimentPostPreSignedUrl(input.fileName))
    }
}
