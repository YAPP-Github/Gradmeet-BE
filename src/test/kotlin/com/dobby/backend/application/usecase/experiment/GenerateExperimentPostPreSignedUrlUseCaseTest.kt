package com.dobby.backend.application.usecase.experiment

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import com.dobby.backend.domain.gateway.S3Gateway
import io.kotest.assertions.throwables.shouldThrow

class GenerateExperimentPostPreSignedUrlUseCaseTest : BehaviorSpec({

    val s3Gateway = mockk<S3Gateway>()
    val useCase = GenerateExperimentPostPreSignedUrlUseCase(s3Gateway)

    given("유효한 파일 이름이 주어졌을 때") {
        val fileName = "test-file.jpg"
        val expectedPreSignedUrl = "https://s3.bucket.url/test-file.jpg"

        `when`("getExperimentPostPreSignedUrl이 호출되면") {
            every { s3Gateway.getExperimentPostPreSignedUrl(fileName) } returns expectedPreSignedUrl

            val result = useCase.execute(GenerateExperimentPostPreSignedUrlUseCase.Input(fileName))

            then("올바른 PreSigned URL이 반환된다") {
                result.preSignedUrl shouldBe expectedPreSignedUrl
            }
        }
    }

    given("빈 파일 이름이 주어졌을 때") {
        val fileName = ""

        `when`("getExperimentPostPreSignedUrl이 호출되면") {
            every { s3Gateway.getExperimentPostPreSignedUrl(fileName) } throws IllegalArgumentException("Invalid file name")

            then("예외가 발생한다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    useCase.execute(GenerateExperimentPostPreSignedUrlUseCase.Input(fileName))
                }
                exception.message shouldBe "Invalid file name"
            }
        }
    }

    given("파일 이름에 확장자가 주어지지 않을 때") {
        val fileName = "test-file"

        `when`("getExperimentPostPreSignedUrl이 호출되면") {
            every { s3Gateway.getExperimentPostPreSignedUrl(fileName) } throws IllegalArgumentException("Invalid file name")

            then("예외가 발생한다") {
                val exception = shouldThrow<IllegalArgumentException> {
                    useCase.execute(GenerateExperimentPostPreSignedUrlUseCase.Input(fileName))
                }
                exception.message shouldBe "Invalid file name"
            }
        }
    }
})
