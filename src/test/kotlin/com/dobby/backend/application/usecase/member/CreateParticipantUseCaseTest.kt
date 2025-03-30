package com.dobby.backend.application.usecase.member

import com.dobby.backend.domain.IdGenerator
import com.dobby.backend.domain.gateway.auth.TokenGateway
import com.dobby.backend.domain.gateway.member.MemberConsentGateway
import com.dobby.backend.domain.gateway.member.ParticipantGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.model.member.MemberConsent
import com.dobby.backend.domain.model.member.Participant
import com.dobby.backend.domain.enums.member.GenderType
import com.dobby.backend.domain.enums.member.ProviderType
import com.dobby.backend.domain.enums.member.RoleType
import com.dobby.backend.domain.enums.areaInfo.Area
import com.dobby.backend.domain.enums.areaInfo.Region
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate

class CreateParticipantUseCaseTest: BehaviorSpec ({

    val participantGateway: ParticipantGateway = mockk()
    val memberConsentGateway: MemberConsentGateway = mockk()
    val tokenGateway: TokenGateway = mockk()
    val idGenerator: IdGenerator = mockk()

    val createParticipantUseCase = CreateParticipantUseCase(participantGateway, memberConsentGateway, tokenGateway, idGenerator)

    given("유효한 입력을 받았을 때") {
        val input = CreateParticipantUseCase.Input(
            oauthEmail = "christer10@naver.com",
            provider = ProviderType.NAVER,
            contactEmail = "christer10@ewhain.net",
            name = "신수정 실험자",
            gender = GenderType.FEMALE,
            birthDate = LocalDate.of(2002, 11, 21),
            basicAddressInfo = CreateParticipantUseCase.AddressInfo(
                region = Region.GYEONGGI,
                area = Area.GWANGMYEONGSI
            ),
            additionalAddressInfo = CreateParticipantUseCase.AddressInfo(
                region = Region.SEOUL,
                area = Area.SEODAEMUNGU
            ),
            matchType = null,
            adConsent = true,
            matchConsent = true
        )

        val member = Member.newMember(
            id = "1",
            oauthEmail = input.oauthEmail,
            contactEmail = input.contactEmail,
            provider = input.provider,
            role = RoleType.PARTICIPANT,
            name = input.name
        )
        val participant = Participant.newParticipant(
            id = "1",
            member = member,
            gender = input.gender,
            birthDate = input.birthDate,
            basicAddressInfo = Participant.AddressInfo(
                region = input.basicAddressInfo.region,
                area = input.basicAddressInfo.area
            ),
            additionalAddressInfo = Participant.AddressInfo(
                region = input.additionalAddressInfo.region,
                area = input.additionalAddressInfo.area
            ),
            matchType = input.matchType
        )

        val memberConsent = MemberConsent.newConsent(
            memberId = member.id,
            adConsent = input.adConsent,
            matchConsent = input.matchConsent,
        )


        val savedParticipant = participant.copy(member = member.copy(id = "1"))
        val accessToken = "mock-access-token"
        val refreshToken = "mock-refresh-token"

        every { idGenerator.generateId() } returns "1"
        every { participantGateway.save(any()) } returns savedParticipant
        every { memberConsentGateway.save(match { it.memberId == member.id }) } returns memberConsent
        every { tokenGateway.generateAccessToken(any()) } returns accessToken
        every { tokenGateway.generateRefreshToken(any()) } returns refreshToken

        When("useCase의 execute가 호출되고, 새로운 참여자가 생성되면") {
            val output = createParticipantUseCase.execute(input)

            Then("엑세스 토큰과 리프레시 토큰이 정상적으로 발급되어야 한다") {
                output.accessToken shouldBe accessToken
                output.refreshToken shouldBe refreshToken
            }
            Then("회원 정보가 올바르게 반환되어야 한다") {
                output.memberInfo.memberId shouldBe savedParticipant.member.id
                output.memberInfo.name shouldBe savedParticipant.member.name
                output.memberInfo.oauthEmail shouldBe savedParticipant.member.oauthEmail
                output.memberInfo.provider shouldBe savedParticipant.member.provider
                output.memberInfo.role shouldBe savedParticipant.member.role
            }
            Then("MemberConsent가 저장되어야 한다") {
                verify { memberConsentGateway.save(match { it.memberId == member.id }) }
            }
        }
    }
})
