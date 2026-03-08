package com.dobby.usecase.experiment

import com.dobby.enums.MatchType
import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import com.dobby.enums.experiment.TimeSlot
import com.dobby.enums.member.GenderType
import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.model.experiment.ApplyMethod
import com.dobby.model.experiment.ExperimentPost
import com.dobby.model.experiment.TargetGroup
import com.dobby.model.member.Member
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime

class GetExperimentPostDetailRelatedUseCaseTest : BehaviorSpec({

    val experimentPostGateway = mockk<ExperimentPostGateway>()
    val useCase = GetExperimentPostDetailRelatedUseCase(experimentPostGateway)

    val member = mockk<Member>()
    every { member.id } returns "1"
    every { member.name } returns "신수정"
    every { member.deletedAt } returns null

    val targetGroup = mockk<TargetGroup>()
    every { targetGroup.id } returns "1"
    every { targetGroup.startAge } returns 18
    every { targetGroup.endAge } returns 30
    every { targetGroup.genderType } returns GenderType.ALL
    every { targetGroup.otherCondition } returns null

    val applyMethod = mockk<ApplyMethod>()
    every { applyMethod.id } returns "1"

    fun createPost(id: String, title: String): ExperimentPost {
        return ExperimentPost(
            id = id,
            title = title,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            member = member,
            views = 100,
            recruitStatus = true,
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
            area = Area.SEODAEMUNGU,
            isOnCampus = true,
            place = "이화여자대학교",
            detailedAddress = "이화여자대학교 아산공학관",
            content = "테스트 내용",
            alarmAgree = false,
            images = mutableListOf()
        )
    }

    given("next 게시글이 존재하는 경우") {

        val nextPost = createPost("2", "다음 공고")
        val prevPost = createPost("0", "이전 공고")

        every { experimentPostGateway.findNextPost("1") } returns nextPost
        every { experimentPostGateway.findPrevPosts("1", 1) } returns listOf(prevPost)

        `when`("둘러보기 공고를 조회하면") {

            val result = useCase.execute(
                GetExperimentPostDetailRelatedUseCase.Input("1")
            )

            then("next 1개 + prev 1개 총 2개가 반환된다") {

                result.postInfo shouldHaveSize 2
                result.postInfo[0].experimentPostId shouldBe "2"
                result.postInfo[1].experimentPostId shouldBe "0"
            }
        }
    }

    given("next 게시글이 없는 경우") {

        val prev1 = createPost("0", "이전 공고1")
        val prev2 = createPost("-1", "이전 공고2")

        every { experimentPostGateway.findNextPost("1") } returns null
        every { experimentPostGateway.findPrevPosts("1", 2) } returns listOf(prev1, prev2)

        `when`("둘러보기 공고를 조회하면") {

            val result = useCase.execute(
                GetExperimentPostDetailRelatedUseCase.Input("1")
            )

            then("이전 공고 2개가 반환된다") {

                result.postInfo shouldHaveSize 2
                result.postInfo[0].experimentPostId shouldBe "0"
                result.postInfo[1].experimentPostId shouldBe "-1"
            }
        }
    }

    given("추천할 공고가 없는 경우") {

        every { experimentPostGateway.findNextPost("1") } returns null
        every { experimentPostGateway.findPrevPosts("1", 2) } returns emptyList()

        `when`("둘러보기 공고를 조회하면") {

            val result = useCase.execute(
                GetExperimentPostDetailRelatedUseCase.Input("1")
            )

            then("빈 리스트가 반환된다") {

                result.postInfo shouldHaveSize 0
            }
        }
    }
})
