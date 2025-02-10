package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.ApplyMethod
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.domain.model.experiment.TargetGroup
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enums.member.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.experiment.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime

class GetMyExperimentPostsUseCaseTest : BehaviorSpec({
    val experimentPostGateway = mockk<ExperimentPostGateway>()
    val useCase = GetMyExperimentPostsUseCase(experimentPostGateway)

    given("유효한 memberId와 pagination 정보가 주어졌을 때") {
        val memberId = "1"
        val paginationInput = GetMyExperimentPostsUseCase.PaginationInput(page = 1, count = 6, order = "DESC")
        val input = GetMyExperimentPostsUseCase.Input(memberId, paginationInput)

        val member = mockk<Member>()
        every { member.name } returns "임도비"

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

        val experimentPostList = listOf(
            ExperimentPost(
                id = "1",
                title = "야뿌들의 평균 식사량 체크 테스트",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                member = member,
                views = 100,
                recruitStatus = false,
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                leadResearcher = "임지수",
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
            ),
            ExperimentPost(
                id = "2",
                title = "다음 세대 인공지능을 위한 연구",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                member = member,
                views = 150,
                recruitStatus = true,
                startDate = LocalDate.now().plusDays(1),
                endDate = LocalDate.now().plusWeeks(2),
                leadResearcher = "임지수 연구자",
                matchType = MatchType.ALL,
                reward = "애플 기프트 카드 5000원",
                count = 5,
                timeRequired = TimeSlot.ABOUT_3H,
                targetGroup = targetGroup,
                applyMethod = applyMethod,
                region = Region.BUSAN,
                area = Area.GEUMJEONGGU,
                univName = "부산대학교",
                detailedAddress = "부산대학교 자연과학대학",
                content = "다음 세대의 AI를 위한 실험 연구입니다.",
                alarmAgree = true,
                images = mutableListOf()
            ),
            ExperimentPost(
                id = "3",
                title = "소셜 미디어 연구 참가자 모집",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                member = member,
                views = 200,
                recruitStatus = false,
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusDays(10),
                leadResearcher = "Dr. Lim",
                matchType = MatchType.OFFLINE,
                reward = "스타벅스 기프트 카드 10000원",
                count = 20,
                timeRequired = TimeSlot.ABOUT_2H,
                targetGroup = targetGroup,
                applyMethod = applyMethod,
                region = Region.SEOUL,
                area = Area.GWANAKGU,
                univName = "서울대학교",
                detailedAddress = "서울대학교 전산관",
                content = "소셜 미디어에 대한 연구에 참여할 모집입니다.",
                alarmAgree = false,
                images = mutableListOf()
            )
        )

        every { experimentPostGateway.findExperimentPostsByMemberIdWithPagination(memberId, any(), any()) } returns experimentPostList

        `when`("useCase의 execute가 호출되면") {
            val result = useCase.execute(input)

            then("정상적으로 experimentPost 정보가 반환된다") {
                result.size shouldBe 3
                result[0].experimentPostId shouldBe "1"
                result[0].title shouldBe "야뿌들의 평균 식사량 체크 테스트"
                result[0].content shouldBe "야뿌들의 한끼 식사량을 체크하는 테스트입니다."
                result[0].views shouldBe 100
                result[0].recruitStatus shouldBe false

                result[1].experimentPostId shouldBe "2"
                result[1].title shouldBe "다음 세대 인공지능을 위한 연구"
                result[1].content shouldBe "다음 세대의 AI를 위한 실험 연구입니다."
                result[1].views shouldBe 150
                result[1].recruitStatus shouldBe true

                result[2].experimentPostId shouldBe "3"
                result[2].title shouldBe "소셜 미디어 연구 참가자 모집"
                result[2].content shouldBe "소셜 미디어에 대한 연구에 참여할 모집입니다."
                result[2].views shouldBe 200
                result[2].recruitStatus shouldBe false
            }
        }
    }

    given("주어진 memberId에 해당하는 실험 공고가 없을 때") {
        val memberId = "1"
        val paginationInput = GetMyExperimentPostsUseCase.PaginationInput(page = 1, count = 6, order = "DESC")
        val input = GetMyExperimentPostsUseCase.Input(memberId, paginationInput)

        every { experimentPostGateway.findExperimentPostsByMemberIdWithPagination(memberId, any(), any()) } returns emptyList()

        `when`("useCase의 execute가 호출되면") {
            val result = useCase.execute(input)

            then("빈 리스트가 반환된다") {
                result.shouldBe(emptyList())
            }
        }
    }
})
