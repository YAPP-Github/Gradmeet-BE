package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.domain.exception.ExperimentPostNotFoundException
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.ApplyMethod
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.domain.model.experiment.TargetGroup
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import com.dobby.backend.infrastructure.database.entity.enums.experiment.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enums.member.*
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime

class GetExperimentPostDetailUseCaseTest : BehaviorSpec({
    val experimentPostGateway = mockk<ExperimentPostGateway>()
    val getExperimentPostDetailUseCase = GetExperimentPostDetailUseCase(experimentPostGateway)

    val targetGroup = mockk<TargetGroup>().apply {
        every { id } returns "tg-1"
        every { startAge } returns 18
        every { endAge } returns 30
        every { genderType } returns GenderType.ALL
        every { otherCondition } returns "특별한 조건 없음"
    }
    val applyMethod = mockk<ApplyMethod>().apply {
        every { id } returns "am-1"
        every { phoneNum } returns "010-1234-5678"
        every { formUrl } returns "http://apply.com/form"
        every { content } returns "지원 방법 상세 설명"
    }

    given("유효한 experimentPostId가 주어졌을 때 (작성자 활성 상태)") {
        val experimentPostId = "1"
        val member = Member(
            id = "1",
            name = "임도비",
            oauthEmail = "im@example.com",
            contactEmail = "im@example.com",
            provider = ProviderType.NAVER,
            status = MemberStatus.ACTIVE,
            role = RoleType.RESEARCHER,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = null
        )

        val experimentPost = ExperimentPost(
            id = experimentPostId,
            title = "실험 공고 테스트",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            member = member,
            views = 100,
            recruitStatus = false,
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            leadResearcher = "Lead",
            matchType = MatchType.ALL,
            reward = "네이버페이 1000원",
            count = 10,
            timeRequired = TimeSlot.ABOUT_1H,
            targetGroup = targetGroup,
            applyMethod = applyMethod,
            region = Region.SEOUL,
            area = Area.GWANGJINGU,
            univName = "건국대학교",
            detailedAddress = "건대입구 123호",
            content = "실험 공고 내용입니다.",
            alarmAgree = false,
            images = mutableListOf()
        )

        every { experimentPostGateway.findById(experimentPostId) } returns experimentPost
        every { experimentPostGateway.save(experimentPost) } returns experimentPost

        `when`("내가 작성한 공고를 대상으로 execute가 호출되면") {
            val input = GetExperimentPostDetailUseCase.Input(experimentPostId = experimentPostId, memberId = member.id)
            val initialViews = experimentPost.views
            val result = getExperimentPostDetailUseCase.execute(input)

            then("isAuthor가 true여야 하고 uploaderStatus는 true여야 한다") {
                result.experimentPostDetailResponse.title shouldBe experimentPost.title
                result.experimentPostDetailResponse.isAuthor shouldBe true
                result.experimentPostDetailResponse.isUploaderActive shouldBe true
            }

            then("views가 1 증가했는지 확인한다") {
                experimentPost.views shouldBe (initialViews + 1)
            }
        }
    }

    given("유효한 experimentPostId가 주어졌을 때 (작성자 탈퇴 상태)") {
        val experimentPostId = "2"
        val deletedMember = Member(
            id = "2",
            name = "탈퇴회원",
            oauthEmail = "deleted@example.com",
            contactEmail = "deleted@example.com",
            provider = ProviderType.NAVER,
            status = MemberStatus.HOLD,
            role = RoleType.RESEARCHER,
            createdAt = LocalDateTime.now().minusDays(10),
            updatedAt = LocalDateTime.now().minusDays(5),
            deletedAt = LocalDateTime.now().minusDays(1)
        )

        val experimentPost = ExperimentPost(
            id = experimentPostId,
            title = "탈퇴 회원 공고",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            member = deletedMember,
            views = 50,
            recruitStatus = false,
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            leadResearcher = "Lead",
            matchType = MatchType.ALL,
            reward = "보상",
            count = 10,
            timeRequired = TimeSlot.ABOUT_1H,
            targetGroup = targetGroup,
            applyMethod = applyMethod,
            region = Region.SEOUL,
            area = Area.GWANGJINGU,
            univName = "어느대학교",
            detailedAddress = "어디구 어딘가",
            content = "탈퇴 회원 공고 내용",
            alarmAgree = false,
            images = mutableListOf()
        )

        every { experimentPostGateway.findById(experimentPostId) } returns experimentPost
        every { experimentPostGateway.save(experimentPost) } returns experimentPost

        `when`("작성자(업로더)가 탈퇴한 공고를 대상으로 execute가 호출되면") {
            val input = GetExperimentPostDetailUseCase.Input(experimentPostId = experimentPostId, memberId = deletedMember.id)
            val result = getExperimentPostDetailUseCase.execute(input)

            then("isAuthor가 true여야 하고 uploaderStatus는 true여야 한다") {
                result.experimentPostDetailResponse.title shouldBe experimentPost.title
                result.experimentPostDetailResponse.isAuthor shouldBe true
                result.experimentPostDetailResponse.isUploaderActive shouldBe false
            }
        }

        `when`("다른 사람이 작성한 공고를 대상으로 execute가 호출되면") {
            val input = GetExperimentPostDetailUseCase.Input(experimentPostId = experimentPostId, memberId = "3")
            val result = getExperimentPostDetailUseCase.execute(input)

            then("isAuthor가 false인 experimentPostDetailResponse가 반환된다") {
                result.experimentPostDetailResponse.title shouldBe experimentPost.title
                result.experimentPostDetailResponse.isAuthor shouldBe false
            }
        }

        `when`("로그인하지 않은 사람이 execute가 호출하면") {
            val input = GetExperimentPostDetailUseCase.Input(experimentPostId = experimentPostId, memberId = null)
            val result = getExperimentPostDetailUseCase.execute(input)

            then("isAuthor가 false인 experimentPostDetailResponse가 반환된다") {
                result.experimentPostDetailResponse.title shouldBe experimentPost.title
                result.experimentPostDetailResponse.isAuthor shouldBe false
            }
        }
    }

    given("유효하지 않은 experimentPostId가 주어졌을 때") {
        val invalidExperimentPostId = "999"
        every { experimentPostGateway.findById(invalidExperimentPostId) } returns null

        `when`("execute가 호출되면") {
            val input = GetExperimentPostDetailUseCase.Input(experimentPostId = invalidExperimentPostId, memberId = "1")

            then("ExperimentPostNotFoundException이 발생한다") {
                val exception = runCatching { getExperimentPostDetailUseCase.execute(input) }.exceptionOrNull()
                exception shouldBe ExperimentPostNotFoundException
            }
        }
    }
})
