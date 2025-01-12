package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.domain.exception.ExperimentPostNotFoundException
import com.dobby.backend.domain.gateway.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.ApplyMethod
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.domain.model.experiment.TargetGroup
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enum.MatchType
import com.dobby.backend.infrastructure.database.entity.enum.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime
import com.dobby.backend.infrastructure.database.entity.enum.GenderType

class GetExperimentPostDetailUseCaseTest : BehaviorSpec({
    val experimentPostGateway = mockk<ExperimentPostGateway>()
    val getExperimentPostDetailUseCase = GetExperimentPostDetailUseCase(experimentPostGateway)

    given("유효한 experimentPostId가 주어졌을 때") {
        val experimentPostId = 1L

        val member = mockk<Member>()
        every { member.name } returns "임도비"

        val targetGroup = mockk<TargetGroup>()
        every { targetGroup.id } returns 1L
        every { targetGroup.startAge } returns 18
        every { targetGroup.endAge } returns 30
        every { targetGroup.genderType } returns GenderType.ALL
        every { targetGroup.otherCondition } returns "특별한 조건 없음"

        val applyMethod = mockk<ApplyMethod>()
        every { applyMethod.id } returns 1L
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
            recruitDone = false,
            startDate = LocalDate.now(),
            endDate = LocalDate.now(),
            leadResearcher = "Lead",
            matchType = MatchType.HYBRID,
            reward = "네이버페이 1000원",
            count = 10,
            durationMinutes = TimeSlot.ABOUT_1H,
            targetGroup = targetGroup,
            applyMethod = applyMethod,
            region = Region.SEOUL,
            area = Area.GWANGJINGU,
            univName = "건국대학교",
            detailedAddress = "건국대학교 공학관",
            content = "야뿌들의 한끼 식사량을 체크하는 테스트입니다.",
            alarmAgree = false,
            images = emptyList()
        )

        every { experimentPostGateway.findById(experimentPostId) } returns experimentPost
        every { experimentPostGateway.save(experimentPost) } returns experimentPost

        `when`("execute가 호출되면") {
            val input = GetExperimentPostDetailUseCase.Input(experimentPostId = experimentPostId)
            val initialViews = experimentPost.views
            val result = getExperimentPostDetailUseCase.execute(input)

            then("experimentPostDetailResponse가 반환된다") {
                result.experimentPostDetailResponse.title shouldBe experimentPost.title
            }

            then("views가 증가했는지 확인한다") {
                experimentPost.views shouldBe (initialViews + 1)
            }
        }
    }

    given("유효하지 않은 experimentPostId가 주어졌을 때") {
        val invalidExperimentPostId = 999L

        every { experimentPostGateway.findById(invalidExperimentPostId) } returns null

        `when`("execute가 호출되면") {
            val input = GetExperimentPostDetailUseCase.Input(experimentPostId = invalidExperimentPostId)

            then("ExperimentPostNotFoundException이 발생한다") {
                val exception = runCatching { getExperimentPostDetailUseCase.execute(input) }.exceptionOrNull()
                exception shouldBe ExperimentPostNotFoundException()
            }
        }
    }
})
