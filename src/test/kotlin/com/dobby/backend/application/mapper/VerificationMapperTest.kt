package com.dobby.backend.application.mapper

import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus
import com.dobby.backend.presentation.api.dto.request.signup.EmailSendRequest
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class VerificationMapperTest : BehaviorSpec({

    given("EmailSendRequest와 인증 코드가 주어졌을 때") {
        val request = EmailSendRequest(univEmail = "test@postech.edu")
        val code = "123456"

        `when`("toEntity 메서드가 호출되면") {
            val result = VerificationMapper.toEntity(request, code)

            then("VerificationEntity 객체를 반환해야 한다") {
                result.univEmail shouldBe request.univEmail
                result.verificationCode shouldBe code
                result.status shouldBe VerificationStatus.HOLD
                result.expiresAt shouldBe null
            }
        }
    }

    given("isSuccess와 message가 주어졌을 때") {
        `when`("toSendResDto 메서드가 호출되면") {
            val isSuccess = true
            val message = "해당 학교 이메일로 성공적으로 코드를 전송했습니다. 10분 이내로 인증을 완료해주세요."
            val result = VerificationMapper.toSendResDto(isSuccess, message)

            then("EmailCodeSendUseCase.Output 객체를 반환해야 한다") {
                result.isSuccess shouldBe isSuccess
                result.message shouldBe message
            }
        }

        `when`("toVerifyResDto 메서드가 호출되면") {
            val isSuccess = true
            val message = "학교 메일 인증이 완료되었습니다."
            val result = VerificationMapper.toVerifyResDto(isSuccess, message)

            then("EmailVerificationUseCase.Output 객체를 반환해야 한다") {
                result.isSuccess shouldBe isSuccess
                result.message shouldBe message
            }
        }
    }
})
