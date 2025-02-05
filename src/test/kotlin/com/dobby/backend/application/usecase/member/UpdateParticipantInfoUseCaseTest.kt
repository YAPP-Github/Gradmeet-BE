package com.dobby.backend.application.usecase.member

import com.dobby.backend.domain.exception.ContactEmailDuplicateException
import com.dobby.backend.domain.exception.ParticipantNotFoundException
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.gateway.member.ParticipantGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.model.member.Participant
import com.dobby.backend.infrastructure.database.entity.enums.*
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalDateTime

class UpdateParticipantInfoUseCaseTest : BehaviorSpec({
    val participantGateway = mockk<ParticipantGateway>()
    val memberGateway = mockk<MemberGateway>()
    val useCase = UpdateParticipantInfoUseCase(participantGateway, memberGateway)

    given("유효한 memberId가 주어졌을 때") {
        val memberId = "1"

        val participant = Participant(
            id = memberId,
            member = Member(id = memberId, name = "기존 이름", contactEmail = "old@example.com", oauthEmail = "oauth@example.com",
                provider = ProviderType.NAVER, role = RoleType.PARTICIPANT, status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
            gender = GenderType.MALE,
            birthDate = LocalDate.of(1998, 5, 10),
            basicAddressInfo = Participant.AddressInfo(Region.SEOUL, Area.SEOUL_ALL),
            additionalAddressInfo = Participant.AddressInfo(Region.INCHEON, Area.SEOGU),
            matchType = MatchType.OFFLINE
        )

        val input = UpdateParticipantInfoUseCase.Input(
            memberId = memberId,
            contactEmail = "new@example.com",
            name = "새로운 이름",
            basicAddressInfo = Participant.AddressInfo(Region.BUSAN, Area.BUSAN_ALL),
            additionalAddressInfo = Participant.AddressInfo(Region.DAEGU, Area.DAEGU_ALL),
            matchType = MatchType.ONLINE
        )

        every { participantGateway.findByMemberId(memberId) } returns participant
        every { memberGateway.existsByContactEmail(input.contactEmail) } returns false
        every { participantGateway.save(any()) } answers { firstArg() }

        `when`("useCase의 execute가 호출되면") {
            val result = useCase.execute(input)

            then("정상적으로 participant 정보가 업데이트된다") {
                result.member.id shouldBe memberId
                result.member.contactEmail shouldBe input.contactEmail
                result.member.name shouldBe input.name
                result.basicAddressInfo.region shouldBe input.basicAddressInfo.region
                result.basicAddressInfo.area shouldBe input.basicAddressInfo.area
                result.additionalAddressInfo?.region shouldBe input.additionalAddressInfo?.region
                result.additionalAddressInfo?.area shouldBe input.additionalAddressInfo?.area
                result.matchType shouldBe input.matchType
            }

            then("Gateway를 통해 save가 호출된다") {
                verify { participantGateway.save(any()) }
            }
        }
    }

    given("존재하지 않는 participantId가 주어졌을 때") {
        val memberId = "1"

        every { participantGateway.findByMemberId(memberId) } returns null

        `when`("useCase의 execute가 호출되면") {
            val input = UpdateParticipantInfoUseCase.Input(
                memberId = memberId,
                contactEmail = "test@example.com",
                name = "테스트",
                basicAddressInfo = Participant.AddressInfo(Region.SEOUL, Area.SEOUL_ALL),
                additionalAddressInfo = null,
                matchType = null
            )

            then("ParticipantNotFoundException이 발생한다") {
                shouldThrow<ParticipantNotFoundException> {
                    useCase.execute(input)
                }
            }
        }
    }

    given("중복된 contactEmail이 주어졌을 때") {
        val memberId = "1"

        val participant = Participant(
            id = memberId,
            member = Member(id = memberId, name = "기존 이름", contactEmail = "old@example.com", oauthEmail = "oauth@example.com",
                provider = ProviderType.NAVER, role = RoleType.PARTICIPANT, status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
            gender = GenderType.MALE,
            birthDate = LocalDate.of(1998, 5, 10),
            basicAddressInfo = Participant.AddressInfo(Region.SEOUL, Area.SEOUL_ALL),
            additionalAddressInfo = Participant.AddressInfo(Region.INCHEON, Area.SEOGU),
            matchType = MatchType.OFFLINE
        )

        val input = UpdateParticipantInfoUseCase.Input(
            memberId = memberId,
            contactEmail = "duplicate@example.com",
            name = "새로운 이름",
            basicAddressInfo = Participant.AddressInfo(Region.BUSAN, Area.BUSAN_ALL),
            additionalAddressInfo = Participant.AddressInfo(Region.DAEGU, Area.DAEGU_ALL),
            matchType = MatchType.ONLINE
        )

        every { participantGateway.findByMemberId(memberId) } returns participant
        every { memberGateway.existsByContactEmail(input.contactEmail) } returns true

        `when`("useCase의 execute가 호출되면") {
            then("ContactEmailDuplicateException이 발생한다") {
                shouldThrow<ContactEmailDuplicateException> {
                    useCase.execute(input)
                }
            }
        }
    }
})
