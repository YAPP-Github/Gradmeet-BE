package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.signupUseCase.CreateResearcherUseCase
import com.dobby.backend.application.usecase.signupUseCase.ParticipantSignupUseCase
import com.dobby.backend.application.usecase.signupUseCase.VerifyResearcherEmailUseCase
import com.dobby.backend.domain.exception.EmailNotValidateException
import com.dobby.backend.infrastructure.database.entity.enum.*
import com.dobby.backend.presentation.api.dto.request.signup.ResearcherSignupRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*


class SignupServiceTest : BehaviorSpec({

    val participantSignupUseCase = mockk<ParticipantSignupUseCase>(relaxed = true)
    val createResearcherUseCase = mockk<CreateResearcherUseCase>(relaxed = true)
    val verifyResearcherEmailUseCase = mockk<VerifyResearcherEmailUseCase>(relaxed = true)

    val signupService = SignupService(
        participantSignupUseCase,
        createResearcherUseCase,
        verifyResearcherEmailUseCase
    )

    beforeTest {
        clearMocks(verifyResearcherEmailUseCase, createResearcherUseCase, participantSignupUseCase)
    }

    given("emailVerified가 false인 researcherSignup input이 주어졌을 때") {
        val invalidInput = CreateResearcherUseCase.Input(
            oauthEmail = "test@example.com",
            provider = ProviderType.GOOGLE,
            contactEmail = "contact@example.com",
            univEmail = "ss@ewha.ac.kr",
            emailVerified = false,
            name = "Test User",
            univName = "이화여자대학교",
            major = "인공지능융합전공",
            labInfo = "분산 학습 아키텍처"
        )

        `when`("SignupService의 researcherSignup이 호출되면") {
            then("EmailNotValidateException이 발생한다") {
                shouldThrow<EmailNotValidateException> {
                    signupService.researcherSignup(invalidInput)
                }

                verify(exactly = 0) { verifyResearcherEmailUseCase.execute(any()) }
                verify(exactly = 0) { createResearcherUseCase.execute(any()) }
            }
        }
    }
})
