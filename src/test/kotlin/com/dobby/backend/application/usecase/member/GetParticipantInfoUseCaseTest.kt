package com.dobby.backend.application.usecase.member

import com.dobby.exception.ParticipantNotFoundException
import com.dobby.gateway.member.MemberConsentGateway
import com.dobby.gateway.member.MemberGateway
import com.dobby.gateway.member.ParticipantGateway
import com.dobby.model.member.Member
import com.dobby.model.member.MemberConsent
import com.dobby.model.member.Participant
import com.dobby.enums.member.GenderType
import com.dobby.enums.MatchType
import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate

class GetParticipantInfoUseCaseTest : BehaviorSpec({
    val memberGateway = mockk<MemberGateway>()
    val participantGateway = mockk<ParticipantGateway>()
    val memberConsentGateway = mockk<MemberConsentGateway>()
    val useCase = GetParticipantInfoUseCase(memberGateway, participantGateway, memberConsentGateway)

    given("유효한 memberId가 주어졌을 때") {
        val memberId = "1"

        val member = mockk<Member>(relaxed = true)
        val participant = mockk<Participant>(relaxed = true)
        val memberConsent = mockk<MemberConsent>(relaxed = true)
        val basicAddressInfo = Participant.AddressInfo(region = Region.SEOUL, area = Area.GWANGJINGU)
        val additionalAddressInfo = Participant.AddressInfo(region = Region.INCHEON, area = Area.SEOGU)

        every { memberGateway.getById(memberId) } returns member
        every { participantGateway.findByMemberId(memberId) } returns participant
        every { memberConsentGateway.findByMemberId(memberId) } returns memberConsent
        every { participant.gender } returns GenderType.FEMALE
        every { participant.birthDate } returns LocalDate.of(2000, 7, 8)
        every { participant.basicAddressInfo } returns basicAddressInfo
        every { participant.additionalAddressInfo } returns additionalAddressInfo
        every { participant.matchType } returns MatchType.ALL

        `when`("useCase의 execute가 호출되면") {
            val input = GetParticipantInfoUseCase.Input(memberId)
            val result = useCase.execute(input)

            then("정상적으로 participant 정보가 반환된다") {
                result.member shouldBe member
                result.gender shouldBe GenderType.FEMALE
                result.birthDate shouldBe LocalDate.of(2000, 7, 8)
                result.basicAddressInfo shouldBe basicAddressInfo
                result.additionalAddressInfo shouldBe additionalAddressInfo
                result.matchType shouldBe MatchType.ALL
            }
        }
    }

    given("존재하지 않는 participantId가 주어졌을 때") {
        val memberId = "1"

        val member = mockk<Member>()

        every { memberGateway.getById(memberId) } returns member
        every { participantGateway.findByMemberId(memberId) } returns null

        `when`("useCase의 execute가 호출되면") {
            val input = GetParticipantInfoUseCase.Input(memberId)

            then("ParticipantNotFoundException이 발생한다") {
                shouldThrow<ParticipantNotFoundException> {
                    useCase.execute(input)
                }
            }
        }
    }
})
