package com.dobby.usecase.member

import com.dobby.enums.MatchType
import com.dobby.exception.ContactEmailDuplicateException
import com.dobby.exception.ParticipantNotFoundException
import com.dobby.gateway.member.MemberConsentGateway
import com.dobby.gateway.member.MemberGateway
import com.dobby.gateway.member.ParticipantGateway
import com.dobby.model.member.Member
import com.dobby.model.member.Participant
import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import com.dobby.enums.member.GenderType
import com.dobby.enums.member.MemberStatus
import com.dobby.enums.member.ProviderType
import com.dobby.enums.member.RoleType
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalDateTime

class UpdateParticipantInfoUseCaseTest : BehaviorSpec({
    val participantGateway = mockk<ParticipantGateway>(relaxed = true)
    val memberGateway = mockk<MemberGateway>(relaxed = true)
    val memberConsentGateway = mockk<MemberConsentGateway>(relaxed = true)
    val useCase = UpdateParticipantInfoUseCase(participantGateway, memberGateway, memberConsentGateway)

    given("유효한 memberId가 주어졌을 때") {
        val memberId = "1"

        val participant = Participant(
            id = memberId,
            member = Member(id = memberId, name = "기존 이름", contactEmail = "old@example.com", oauthEmail = "oauth@example.com",
                provider = ProviderType.NAVER, role = RoleType.PARTICIPANT, status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now(), deletedAt = null),
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
            matchType = MatchType.ONLINE,
            adConsent = true,
            matchConsent = false
        )

        every { participantGateway.findByMemberId(memberId) } returns participant
        every { memberConsentGateway.save(any()) } returnsArgument 0
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
            then("memberConsent가 호출된다") {
                shouldNotThrowAny {
                    verify { memberConsentGateway.save(any()) }
                }
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
                matchType = null,
                adConsent = true,
                matchConsent = true
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

        every { participantGateway.findByMemberId(memberId) } returns mockk(relaxed = true)
        every { memberGateway.existsByContactEmail("duplicate@example.com") } returns true

        val participant = Participant(
            id = memberId,
            member = Member(id = memberId, name = "기존 이름", contactEmail = "old@example.com", oauthEmail = "oauth@example.com",
                provider = ProviderType.NAVER, role = RoleType.PARTICIPANT, status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now(), deletedAt = null),
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
            matchType = MatchType.ONLINE,
            adConsent = true,
            matchConsent = false
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

    given("사용자가 자신의 기존 contactEmail을 사용할 때") {
        val memberId = "1"
        val existingEmail = "existing@example.com"

        val participant = Participant(
            id = memberId,
            member = Member(
                id = memberId,
                name = "기존 이름",
                contactEmail = existingEmail,
                oauthEmail = "oauth@example.com",
                provider = ProviderType.NAVER,
                role = RoleType.PARTICIPANT,
                status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                deletedAt = null
            ),
            gender = GenderType.MALE,
            birthDate = LocalDate.of(1998, 5, 10),
            basicAddressInfo = Participant.AddressInfo(Region.SEOUL, Area.SEOUL_ALL),
            additionalAddressInfo = Participant.AddressInfo(Region.INCHEON, Area.SEOGU),
            matchType = MatchType.OFFLINE
        )

        val input = UpdateParticipantInfoUseCase.Input(
            memberId = memberId,
            contactEmail = existingEmail,
            name = "새로운 이름",
            basicAddressInfo = Participant.AddressInfo(Region.BUSAN, Area.BUSAN_ALL),
            additionalAddressInfo = Participant.AddressInfo(Region.DAEGU, Area.DAEGU_ALL),
            matchType = MatchType.ONLINE,
            adConsent = true,
            matchConsent = false
        )

        every { participantGateway.findByMemberId(memberId) } returns participant
        every { memberGateway.existsByContactEmail(input.contactEmail) } returns true
        every { participantGateway.save(any()) } answers { firstArg() }

        `when`("useCase의 execute가 호출되면") {
            val result = useCase.execute(input)

            then("정상적으로 participant 정보가 업데이트된다") {
                result.member.contactEmail shouldBe existingEmail
            }
        }
    }
})
