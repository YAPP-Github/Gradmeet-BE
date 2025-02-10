package com.dobby.backend.application.usecase.member

import com.dobby.backend.domain.exception.ParticipantNotFoundException
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.gateway.member.ParticipantGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.model.member.Participant
import com.dobby.backend.infrastructure.database.entity.enums.member.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate

class GetParticipantInfoUseCaseTest : BehaviorSpec({
    val memberGateway = mockk<MemberGateway>()
    val participantGateway = mockk<ParticipantGateway>()
    val useCase = GetParticipantInfoUseCase(memberGateway, participantGateway)

    given("유효한 memberId가 주어졌을 때") {
        val memberId = "1"

        val member = mockk<Member>()
        val participant = mockk<Participant>()
        val basicAddressInfo = Participant.AddressInfo(region = Region.SEOUL, area = Area.GWANGJINGU)
        val additionalAddressInfo = Participant.AddressInfo(region = Region.INCHEON, area = Area.SEOGU)

        every { memberGateway.getById(memberId) } returns member
        every { participantGateway.findByMemberId(memberId) } returns participant
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
