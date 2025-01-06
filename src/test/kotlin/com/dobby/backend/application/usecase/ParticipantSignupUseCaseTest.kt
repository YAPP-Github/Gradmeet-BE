package com.dobby.backend.application.usecase

import com.dobby.backend.application.mapper.SignupMapper
import com.dobby.backend.infrastructure.database.entity.ParticipantEntity
import com.dobby.backend.infrastructure.database.entity.enum.GenderType
import com.dobby.backend.infrastructure.database.entity.enum.MatchType
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import com.dobby.backend.infrastructure.database.repository.ParticipantRepository
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.presentation.api.dto.request.signup.ParticipantSignupRequest
import com.dobby.backend.presentation.api.dto.request.signup.AddressInfo as DtoAddressInfo
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate

class ParticipantSignupUseCaseTest : BehaviorSpec({

    given("유효한 ParticipantSignupRequest가 주어졌을 때") {
        val participantRepository = mockk<ParticipantRepository>()
        val jwtTokenProvider = mockk<JwtTokenProvider>()
        val useCase = ParticipantSignupUseCase(participantRepository, jwtTokenProvider)

        val request = ParticipantSignupRequest(
            oauthEmail = "test@example.com",
            provider = ProviderType.GOOGLE,
            contactEmail = "contact@example.com",
            name = "Test User",
            birthDate = LocalDate.of(2002, 11, 21),
            basicAddressInfo = DtoAddressInfo(region = Region.SEOUL, area = Area.SEOUL_ALL),
            additionalAddressInfo = null,
            preferType = MatchType.HYBRID,
            gender = GenderType.FEMALE
        )

        val member = SignupMapper.toMember(request)
        val participant = SignupMapper.toParticipant(member, request)

        every { participantRepository.save(any<ParticipantEntity>()) } returns participant
        every { jwtTokenProvider.generateAccessToken(any()) } returns "mock-access-token"
        every { jwtTokenProvider.generateRefreshToken(any()) } returns "mock-refresh-token"

        `when`("ParticipantSignupUseCase가 실행되면") {
            val response = useCase.execute(request)

            then("ParticipantRepository에 엔티티가 저장되고, SignupResponse가 반환되어야 한다") {
                response.accessToken shouldBe "mock-access-token"
                response.refreshToken shouldBe "mock-refresh-token"
                response.memberInfo.oauthEmail shouldBe "test@example.com"
                response.memberInfo.name shouldBe "Test User"

                // 엔티티 저장
                verify(exactly = 1) { participantRepository.save(any<ParticipantEntity>()) }

                // JWT 토큰 발급
                verify(exactly = 1) { jwtTokenProvider.generateAccessToken(any()) }
                verify(exactly = 1) { jwtTokenProvider.generateRefreshToken(any()) }
            }
        }
    }
})
