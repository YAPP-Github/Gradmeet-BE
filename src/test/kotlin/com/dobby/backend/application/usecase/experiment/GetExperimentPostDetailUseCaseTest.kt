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

    given("유효한 experimentPostId가 주어졌을 때") {
        val experimentPostId = "1"

        val member = mockk<Member>()
        every { member.name } returns "임도비"
        every { member.id } returns "1"
        every { member.deletedAt } returns null

        val targetGroup = mockk<TargetGroup>()
        every { targetGroup.id } returns "1"
        every { targetGroup.startAge } returns 18
        every { targetGroup.endAge } returns 30
        every { targetGroup.genderType } returns GenderType.ALL
        every { targetGroup.otherCondition } returns "특별한 조건 없음"

        val applyMethod = mockk<ApplyMethod>()
        every { applyMethod.id } returns "1"
        every { applyMethod.phoneNum } returns "010-1234-5678"
        every { applyMethod.formUrl } returns "http://apply.com/form"
        every { applyMethod.content } returns "지원 방법에 대한 상세 설명"

        val experimentPost = ExperimentPost(
            id = experimentPostId,
            title = "야뿌들의 평균 식사량 체크 테스트",
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
            detailedAddress = "건국대학교 공학관",
            content = "야뿌들의 한끼 식사량을 체크하는 테스트입니다.",
            alarmAgree = false,
            images = mutableListOf()
        )

        every { experimentPostGateway.findById(experimentPostId) } returns experimentPost
        every { experimentPostGateway.save(experimentPost) } returns experimentPost

        `when`("내가 작성한 공고를 대상으로 execute가 호출되면") {
            val input = GetExperimentPostDetailUseCase.Input(experimentPostId = experimentPostId, memberId = member.id)
            val initialViews = experimentPost.views
            val result = getExperimentPostDetailUseCase.execute(input)

            then("isAuthor가 true인 experimentPostDetail이 반환된다") {
                result.experimentPostDetail.title shouldBe experimentPost.title
                result.experimentPostDetail.isAuthor shouldBe true
            }

            then("views가 증가했는지 확인한다") {
                experimentPost.views shouldBe (initialViews + 1)
            }
        }

        `when`("다른 사람이 작성한 공고를 대상으로 execute가 호출되면") {
            val input = GetExperimentPostDetailUseCase.Input(experimentPostId = experimentPostId, memberId = "2")
            val result = getExperimentPostDetailUseCase.execute(input)

            then("isAuthor가 false인 experimentPostDetail이 반환된다") {
                result.experimentPostDetail.title shouldBe experimentPost.title
                result.experimentPostDetail.isAuthor shouldBe false
            }
        }

        `when`("로그인하지 않은 사람이 execute가 호출하면") {
            val input = GetExperimentPostDetailUseCase.Input(experimentPostId = experimentPostId, memberId = null)
            val result = getExperimentPostDetailUseCase.execute(input)

            then("isAuthor가 false인 experimentPostDetail이 반환된다") {
                result.experimentPostDetail.title shouldBe experimentPost.title
                result.experimentPostDetail.isAuthor shouldBe false
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
