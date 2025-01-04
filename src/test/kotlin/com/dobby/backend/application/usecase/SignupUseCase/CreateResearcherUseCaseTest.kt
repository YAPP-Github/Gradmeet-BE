package com.dobby.backend.application.usecase.SignupUseCase

import com.dobby.backend.application.mapper.SignupMapper
import com.dobby.backend.infrastructure.database.entity.MemberEntity
import com.dobby.backend.infrastructure.database.entity.ResearcherEntity
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.infrastructure.database.repository.ResearcherRepository
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.presentation.api.dto.request.signup.ResearcherSignupRequest
import com.dobby.backend.presentation.api.dto.response.signup.SignupResponse
import com.dobby.backend.util.AuthenticationUtils
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class CreateResearcherUseCaseTest: BehaviorSpec ({

    given("유효한 ResearcherSignupRequest가 주어졌을 때") {

        val researcherRepository = mockk<ResearcherRepository>(relaxed = true)
        val jwtTokenProvider = mockk<JwtTokenProvider>(relaxed = true)
        val useCase = CreateResearcherUseCase(researcherRepository , jwtTokenProvider)

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

        val member = SignupMapper.toResearcherMember(request)
        val newResearcher = SignupMapper.toResearcher(member, request)

        every { researcherRepository.save(any<ResearcherEntity>()) } returns newResearcher
        every { jwtTokenProvider.generateAccessToken(any()) } returns "mock-access-token"
        every { jwtTokenProvider.generateRefreshToken(any()) } returns "mock-refresh-token"

        `when`("CreateResearcherUseCase가 실행되면") {
            val response: SignupResponse = useCase.execute(request)

            then("ResearcherRepository에 엔티티가 저장되고, 올바른 MemberResponse가 반환되어야 한다") {
                response.accessToken shouldBe "mock-access-token"
                response.refreshToken shouldBe "mock-refresh-token"
                response.memberInfo.oauthEmail shouldBe "test@example.com"
                response.memberInfo.name shouldBe "Test User"
                response.memberInfo.role shouldBe RoleType.RESEARCHER
                response.memberInfo.provider shouldBe ProviderType.GOOGLE

                verify(exactly = 1) { researcherRepository.save(any<ResearcherEntity>()) }
                verify(exactly = 1) { jwtTokenProvider.generateAccessToken(any()) }
                verify (exactly = 1){ jwtTokenProvider.generateRefreshToken(any()) }
            }
        }
    }
})
