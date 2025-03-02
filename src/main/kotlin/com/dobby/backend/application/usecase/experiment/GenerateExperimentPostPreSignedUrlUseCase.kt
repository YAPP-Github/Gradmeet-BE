package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.domain.gateway.S3Gateway

class GenerateExperimentPostPreSignedUrlUseCase(
    private val s3Gateway: S3Gateway
): UseCase<GenerateExperimentPostPreSignedUrlUseCase.Input, GenerateExperimentPostPreSignedUrlUseCase.Output> {
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
