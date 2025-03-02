package com.dobby.usecase.experiment

import com.dobby.exception.ExperimentPostNotFoundException
import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.model.experiment.ApplyMethod
import com.dobby.model.experiment.ExperimentPost
import com.dobby.model.experiment.TargetGroup
import com.dobby.model.member.Member
import com.dobby.enums.MatchType
import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import com.dobby.enums.experiment.TimeSlot
import com.dobby.enums.member.GenderType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime

class GetExperimentPostDetailForUpdateUseCaseTest : BehaviorSpec({
    val experimentPostGateway = mockk<ExperimentPostGateway>()
    val getExperimentPostDetailForUpdateUseCase = GetExperimentPostDetailForUpdateUseCase(experimentPostGateway)

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

    val experimentPostId = "1"
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
        place = "건국대학교",
        detailedAddress = "건국대학교 공학관",
        content = "야뿌들의 한끼 식사량을 체크하는 테스트입니다.",
        alarmAgree = false,
        images = mutableListOf()
    )

    given("유효한 experimentPostId와 작성자 본인인 경우") {
        every { experimentPostGateway.findExperimentPostByMemberIdAndPostId("1", experimentPostId) } returns experimentPost

        `when`("작성자가 수정용 상세 정보를 요청하면") {
            val input = GetExperimentPostDetailForUpdateUseCase.Input(
                experimentPostId = experimentPostId,
                memberId = member.id
            )
            val result = getExperimentPostDetailForUpdateUseCase.execute(input)

            then("업데이트를 위한 상세 정보가 정상적으로 반환된다") {
                result.experimentPostDetail.title shouldBe experimentPost.title
                result.experimentPostDetail.isAuthor shouldBe true
            }
        }
    }

    given("유효한 experimentPostId지만 작성자가 아닌 경우") {
        every { experimentPostGateway.findExperimentPostByMemberIdAndPostId("2", experimentPostId) } returns null

        `when`("다른 사용자가 수정용 상세 정보를 요청하면") {
            val input = GetExperimentPostDetailForUpdateUseCase.Input(
                experimentPostId = experimentPostId,
                memberId = "2" // 작성자와 다른 아이디
            )

            then("ExperimentPostNotFoundException이 발생한다") {
                val exception = runCatching { getExperimentPostDetailForUpdateUseCase.execute(input) }.exceptionOrNull()
                exception shouldBe ExperimentPostNotFoundException
            }
        }
    }

    given("유효하지 않은 experimentPostId가 주어진 경우") {
        val invalidExperimentPostId = "999"
        every { experimentPostGateway.findExperimentPostByMemberIdAndPostId("1", invalidExperimentPostId) } returns null

        `when`("수정용 상세 정보를 요청하면") {
            val input = GetExperimentPostDetailForUpdateUseCase.Input(
                experimentPostId = invalidExperimentPostId,
                memberId = member.id
            )

            then("ExperimentPostNotFoundException이 발생한다") {
                val exception = runCatching { getExperimentPostDetailForUpdateUseCase.execute(input) }.exceptionOrNull()
                exception shouldBe ExperimentPostNotFoundException
            }
        }
    }
})
