package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.SignupUseCase.CreateResearcherUseCase
import com.dobby.backend.application.usecase.SignupUseCase.ParticipantSignupUseCase
import com.dobby.backend.application.usecase.SignupUseCase.VerifyResearcherEmailUseCase
import com.dobby.backend.domain.exception.EmailNotValidateException
import com.dobby.backend.infrastructure.database.entity.VerificationEntity
import com.dobby.backend.infrastructure.database.entity.enum.*
import com.dobby.backend.presentation.api.dto.request.signup.ParticipantSignupRequest
import com.dobby.backend.presentation.api.dto.request.signup.ResearcherSignupRequest
import com.dobby.backend.presentation.api.dto.response.signup.SignupResponse
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import com.dobby.backend.infrastructure.database.repository.MemberRepository
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.presentation.api.dto.request.signup.AddressInfo
import com.dobby.backend.presentation.api.dto.response.MemberResponse
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.*
import java.time.LocalDate

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

    given("emailVerified가 false인 ResearcherSignupRequest가 주어졌을 때") {
        val invalidRequest = ResearcherSignupRequest(
            oauthEmail = "test@example.com",
            provider = ProviderType.GOOGLE,
            contactEmail = "contact@example.com",
            univEmail = "ss@ewha.ac.kr",
            emailVerified = false,
            name = "Test User",
            univName = "이화여자대학교",
            major = "인공지능･소프트웨어학부 인공지능융합전공",
            labInfo = "불안정한 통신 상황에서 자생적 엣지 네트워크 구성을 통한 분산 학습 아키텍처 개발"
        )

        `when`("SignupService의 researcherSignup이 호출되면") {
            println("테스트 실행: emailVerified = ${invalidRequest.emailVerified}, univEmail = ${invalidRequest.univEmail}")

            then("EmailNotValidateException이 발생한다") {
                shouldThrow<EmailNotValidateException> {
                    signupService.researcherSignup(invalidRequest)
                }
                verify(exactly = 0) { verifyResearcherEmailUseCase.execute(any()) }
            }
        }
    }
})

