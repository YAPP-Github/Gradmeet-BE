package com.dobby.usecase.experiment

import com.dobby.exception.ExperimentPostNotFoundException
import com.dobby.exception.ExperimentPostRecruitStatusException
import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.model.experiment.ApplyMethod
import com.dobby.model.experiment.ExperimentPost
import com.dobby.model.experiment.TargetGroup
import com.dobby.enums.member.GenderType
import com.dobby.enums.experiment.TimeSlot
import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class UpdateExperimentPostRecruitStatusUseCaseTest : BehaviorSpec({

    val experimentPostGateway = mockk<ExperimentPostGateway>()
    val useCase = UpdateExperimentPostRecruitStatusUseCase(experimentPostGateway)

    given("유효한 memberId와 postId가 주어졌을 때") {
        val memberId = "1"
        val postId = "1"

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

        val post = ExperimentPost(
            id = postId,
            member = mockk(),
            targetGroup = targetGroup,
            applyMethod = applyMethod,
            views = 0,
            title = "야뿌의 한 끼 식사량을 알아봅시다",
            content = "과연 야뿌는 한 끼에 얼마나 먹을까요?",
            leadResearcher = "서버 지수",
            reward = "서버 개발자의 사랑",
            startDate = null,
            endDate = null,
            timeRequired = TimeSlot.ABOUT_1H,
            count = 10,
            matchType = mockk(),
            place = "야뿌대학교",
            region = Region.SEOUL,
            area = Area.GANGNAMGU,
            detailedAddress = "서버학과 연구소",
            alarmAgree = true,
            recruitStatus = true,
            images = mutableListOf(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        every { experimentPostGateway.findExperimentPostByMemberIdAndPostId(memberId, postId) } returns post
        every { experimentPostGateway.save(any()) } returns post.copy(recruitStatus = false)

        `when`("useCase의 execute가 호출되면") {
            val input = UpdateExperimentPostRecruitStatusUseCase.Input(memberId, postId)
            val result = useCase.execute(input)

            then("모집 상태가 false로 업데이트된 experimentPost가 반환된다") {
                result.experimentPost.recruitStatus shouldBe false
            }
        }
    }

    given("모집 상태가 이미 false인 post가 주어졌을 때") {
        val memberId = "1"
        val postId = "1"

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

        val post = ExperimentPost(
            id = postId,
            member = mockk(),
            targetGroup = targetGroup,
            applyMethod = applyMethod,
            views = 0,
            title = "야뿌의 한 끼 식사량을 알아봅시다",
            content = "과연 야뿌는 한 끼에 얼마나 먹을까요?",
            leadResearcher = "서버 지수",
            reward = "서버 개발자의 사랑",
            startDate = null,
            endDate = null,
            timeRequired = TimeSlot.ABOUT_1H,
            count = 10,
            matchType = mockk(),
            place = "야뿌대학교",
            region = Region.SEOUL,
            area = Area.GANGNAMGU,
            detailedAddress = "서버학과 연구소",
            alarmAgree = true,
            recruitStatus = false,
            images = mutableListOf(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        every { experimentPostGateway.findExperimentPostByMemberIdAndPostId(memberId, postId) } returns post

        `when`("execute가 호출되면") {
            val input = UpdateExperimentPostRecruitStatusUseCase.Input(memberId, postId)

            then("ExperimentPostRecruitStatusException이 발생한다") {
                shouldThrow<ExperimentPostRecruitStatusException> {
                    useCase.execute(input)
                }
            }
        }
    }

    given("존재하지 않는 memberId와 postId가 주어졌을 때") {
        val memberId = "999"
        val postId = "999"

        every { experimentPostGateway.findExperimentPostByMemberIdAndPostId(memberId, postId) } returns null

        `when`("execute가 호출되면") {
            val input = UpdateExperimentPostRecruitStatusUseCase.Input(memberId, postId)

            then("ExperimentPostNotFoundException이 발생한다") {
                shouldThrow<ExperimentPostNotFoundException> {
                    useCase.execute(input)
                }
            }
        }
    }
})
