package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.domain.exception.ExperimentPostNotFoundException
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.ApplyMethod
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.domain.model.experiment.TargetGroup
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.experiment.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime

class GetExperimentPostApplyMethodUseCaseTest : BehaviorSpec({
    val experimentPostGateway = mockk<ExperimentPostGateway>()
    val useCase = GetExperimentPostApplyMethodUseCase(experimentPostGateway)

    given("유효한 experimentPostId가 주어졌을 때") {
        val experimentPostId = "1"

        val member = mockk<Member>()
        val targetGroup = mockk<TargetGroup>()
        val applyMethod = ApplyMethod(
            id = "1",
            phoneNum = "123-456-7890",
            formUrl = "https://example.com",
            content = "지원 방법에 대한 상세 설명"
        )
        val mockExperimentPost = ExperimentPost(
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
            place = "건국대학교",
            detailedAddress = "건국대학교 공학관",
            content = "야뿌들의 한끼 식사량을 체크하는 테스트입니다.",
            alarmAgree = false,
            images = mutableListOf()
        )

        every { experimentPostGateway.findById(experimentPostId) } returns mockExperimentPost

        `when`("useCase의 execute가 호출되면") {
            val input = GetExperimentPostApplyMethodUseCase.Input(experimentPostId)
            val result = useCase.execute(input)

            then("applyMethod 정보가 반환된다") {
                result.applyMethodId shouldBe applyMethod.id
                result.phoneNum shouldBe applyMethod.phoneNum
                result.formUrl shouldBe applyMethod.formUrl
                result.content shouldBe applyMethod.content
            }
        }
    }

    given("존재하지 않는 experimentPostId가 주어졌을 때") {
        val invalidExperimentPostId = "999"

        every { experimentPostGateway.findById(invalidExperimentPostId) } returns null

        `when`("useCase의 execute가 호출되면") {
            val input = GetExperimentPostApplyMethodUseCase.Input(invalidExperimentPostId)

            then("ExperimentPostNotFoundException이 발생한다") {
                shouldThrow<ExperimentPostNotFoundException> {
                    useCase.execute(input)
                }
            }
        }
    }
})
