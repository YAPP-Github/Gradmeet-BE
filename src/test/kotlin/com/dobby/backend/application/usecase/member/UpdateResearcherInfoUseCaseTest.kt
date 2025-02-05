package com.dobby.backend.application.usecase.member

import com.dobby.backend.domain.exception.ContactEmailDuplicateException
import com.dobby.backend.domain.exception.ResearcherNotFoundException
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.gateway.member.ResearcherGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.model.member.Researcher
import com.dobby.backend.infrastructure.database.entity.enums.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enums.ProviderType
import com.dobby.backend.infrastructure.database.entity.enums.RoleType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

class UpdateResearcherInfoUseCaseTest : BehaviorSpec({
    val researcherGateway = mockk<ResearcherGateway>()
    val memberGateway = mockk<MemberGateway>()
    val useCase = UpdateResearcherInfoUseCase(researcherGateway, memberGateway)

    given("유효한 memberId가 주어졌을 때") {
        val memberId = "1"

        val researcher = Researcher(
            id = memberId,
            member = Member(
                id = memberId, name = "기존 연구자", contactEmail = "old@example.com",
                oauthEmail = "oauth@example.com", provider = ProviderType.NAVER, role = RoleType.RESEARCHER,
                status = MemberStatus.ACTIVE, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()
            ),
            univEmail = "old@university.com",
            univName = "Old University",
            major = "Old Major",
            labInfo = "Old Lab",
            emailVerified = true
        )

        val input = UpdateResearcherInfoUseCase.Input(
            memberId = memberId,
            contactEmail = "new@example.com",
            name = "새 연구자",
            univName = "New University",
            major = "New Major",
            labInfo = "New Lab"
        )

        every { researcherGateway.findByMemberId(memberId) } returns researcher
        every { memberGateway.existsByContactEmail(input.contactEmail) } returns false
        every { researcherGateway.save(any()) } answers { firstArg() }

        `when`("useCase의 execute가 호출되면") {
            val result = useCase.execute(input)

            then("정상적으로 researcher 정보가 업데이트된다") {
                result.member.id shouldBe memberId
                result.member.contactEmail shouldBe input.contactEmail
                result.member.name shouldBe input.name
                result.univName shouldBe input.univName
                result.major shouldBe input.major
                result.labInfo shouldBe input.labInfo
            }

            then("Gateway를 통해 save가 호출된다") {
                verify { researcherGateway.save(any()) }
            }
        }
    }

    given("존재하지 않는 researcherId가 주어졌을 때") {
        val memberId = "1"

        every { researcherGateway.findByMemberId(memberId) } returns null

        `when`("useCase의 execute가 호출되면") {
            val input = UpdateResearcherInfoUseCase.Input(
                memberId = memberId,
                contactEmail = "test@example.com",
                name = "테스트",
                univName = "University",
                major = "Major",
                labInfo = null
            )

            then("ResearcherNotFoundException이 발생한다") {
                shouldThrow<ResearcherNotFoundException> {
                    useCase.execute(input)
                }
            }
        }
    }

    given("중복된 contactEmail이 주어졌을 때") {
        val memberId = "1"

        val researcher = Researcher(
            id = memberId,
            member = Member(
                id = memberId, name = "기존 연구자", contactEmail = "old@example.com",
                oauthEmail = "oauth@example.com", provider = ProviderType.NAVER, role = RoleType.RESEARCHER,
                status = MemberStatus.ACTIVE, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()
            ),
            univEmail = "old@university.com",
            univName = "Old University",
            major = "Old Major",
            labInfo = "Old Lab",
            emailVerified = true
        )

        val input = UpdateResearcherInfoUseCase.Input(
            memberId = memberId,
            contactEmail = "duplicate@example.com",
            name = "새 연구자",
            univName = "New University",
            major = "New Major",
            labInfo = "New Lab"
        )

        every { researcherGateway.findByMemberId(memberId) } returns researcher
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

        val researcher = Researcher(
            id = memberId,
            member = Member(
                id = memberId,
                name = "기존 연구자",
                contactEmail = existingEmail,
                oauthEmail = "oauth@example.com",
                provider = ProviderType.NAVER,
                role = RoleType.RESEARCHER,
                status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            univEmail = "old@university.com",
            univName = "Old University",
            major = "Old Major",
            labInfo = "Old Lab",
            emailVerified = true
        )

        val input = UpdateResearcherInfoUseCase.Input(
            memberId = memberId,
            contactEmail = existingEmail,
            name = "새 연구자",
            univName = "New University",
            major = "New Major",
            labInfo = "New Lab"
        )

        every { researcherGateway.findByMemberId(memberId) } returns researcher
        every { memberGateway.existsByContactEmail(input.contactEmail) } returns true
        every { researcherGateway.save(any()) } answers { firstArg() }

        `when`("useCase의 execute가 호출되면") {
            val result = useCase.execute(input)

            then("정상적으로 researcher 정보가 업데이트된다") {
                result.member.contactEmail shouldBe existingEmail
                result.member.name shouldBe input.name
                result.univName shouldBe input.univName
                result.major shouldBe input.major
                result.labInfo shouldBe input.labInfo
            }
        }
    }
})
