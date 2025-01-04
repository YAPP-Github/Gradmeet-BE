package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.SignupUseCase.CreateResearcherUseCase
import com.dobby.backend.application.usecase.SignupUseCase.ParticipantSignupUseCase
import com.dobby.backend.domain.exception.EmailNotValidateException
import com.dobby.backend.infrastructure.database.entity.enum.GenderType
import com.dobby.backend.infrastructure.database.entity.enum.MatchType
import com.dobby.backend.presentation.api.dto.request.signup.ParticipantSignupRequest
import com.dobby.backend.presentation.api.dto.request.signup.ResearcherSignupRequest
import com.dobby.backend.presentation.api.dto.response.signup.SignupResponse
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import com.dobby.backend.presentation.api.dto.request.signup.AddressInfo
import com.dobby.backend.presentation.api.dto.response.MemberResponse
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate

class SignupServiceTest : BehaviorSpec({

    val participantSignupUseCase = mockk<ParticipantSignupUseCase>()
    val createResearcherUseCase = mockk<CreateResearcherUseCase>()
    val signupService = SignupService(participantSignupUseCase, createResearcherUseCase)

    given("유효한 ParticipantSignupRequest가 주어졌을 때") {
        val request = ParticipantSignupRequest(
            oauthEmail = "test@example.com",
            provider = ProviderType.GOOGLE,
            contactEmail = "contact@example.com",
            name = "Test User",
            birthDate = LocalDate.of(2002, 11, 21),
            basicAddressInfo = AddressInfo(region = Region.SEOUL, area = Area.SEOUL_ALL),
            additionalAddressInfo = null,
            preferType = MatchType.HYBRID,
            gender = GenderType.FEMALE
        )

        val response = SignupResponse(
            accessToken = "mock-access-token",
            refreshToken = "mock-refresh-token",
            memberInfo = MemberResponse(
                memberId = 1L,
                oauthEmail = "test@example.com",
                name = "Test User",
                provider = ProviderType.GOOGLE,
                role = RoleType.PARTICIPANT
            )
        )

        every { participantSignupUseCase.execute(request) } returns response

        `when`("SignupService의 participantSignup이 호출되면") {
            val result = signupService.participantSignup(request)

            then("ParticipantSignupUseCase가 실행되고 올바른 SignupResponse가 반환된다") {
                result shouldBe response
                verify(exactly = 1) { participantSignupUseCase.execute(request) }
            }
        }
    }

    given("유효한 ResearcherSignupRequest가 주어졌을 때") {
        val request = ResearcherSignupRequest(
            oauthEmail = "test@example.com",
            provider = ProviderType.GOOGLE,
            contactEmail = "contact@example.com",
            univEmail = "univ@ewha.ac.kr",
            emailVerified = true,
            name = "Test User",
            univName = "이화여자대학교",
            major = "인공지능･소프트웨어학부 인공지능융합전공",
            labInfo = "불안정한 통신 상황에서 자생적 엣지 네트워크 구성을 통한 분산 학습 아키텍처 개발"
        )

        val response = SignupResponse(
            accessToken = "mock-access-token",
            refreshToken = "mock-refresh-token",
            memberInfo = MemberResponse(
                memberId = 1L,
                oauthEmail = "test@example.com",
                name = "Test User",
                provider = ProviderType.GOOGLE,
                role = RoleType.PARTICIPANT
            )
        )

        every { createResearcherUseCase.execute(request) } returns response

        `when`("SignupService의 researcherSignup이 호출되면") {
            val result = signupService.researcherSignup(request)

            then("CreateResearcherUseCase가 실행되고 올바른 SignupResponse가 반환된다") {
                result shouldBe response
                verify(exactly = 1) { createResearcherUseCase.execute(request) }
            }
        }
    }

    given("emailVerified가 false인 ResearcherSignupRequest가 주어졌을 때") {
        val invalidReq = ResearcherSignupRequest(
            oauthEmail = "test@example.com",
            provider = ProviderType.GOOGLE,
            contactEmail = "contact@example.com",
            univEmail = "univ@ewha.ac.kr",
            emailVerified = false,
            name = "Test User",
            univName = "이화여자대학교",
            major = "인공지능･소프트웨어학부 인공지능융합전공",
            labInfo = "불안정한 통신 상황에서 자생적 엣지 네트워크 구성을 통한 분산 학습 아키텍처 개발"
        )

        `when`("SignupService의 researcherSignup이 호출되면") {
            then("EmailNotValidateException이 발생한다") {
                shouldThrow<EmailNotValidateException> {
                    signupService.researcherSignup(invalidReq)
                }
                verify(exactly = 0) { createResearcherUseCase.execute(eq(invalidReq)) }
            }
        }
    }
})
