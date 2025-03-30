package com.dobby.backend.application.usecase.member.email

import com.dobby.backend.domain.enums.MatchType
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.*
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.enums.areaInfo.Area
import com.dobby.backend.domain.enums.areaInfo.Region
import com.dobby.backend.domain.enums.experiment.TimeSlot
import com.dobby.backend.domain.enums.member.GenderType
import com.dobby.backend.domain.enums.member.MemberStatus
import com.dobby.backend.domain.enums.member.ProviderType
import com.dobby.backend.domain.enums.member.RoleType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.*

class GetMatchingExperimentPostsUseCaseTest : BehaviorSpec({

    val experimentPostGateway = mockk<ExperimentPostGateway>()
    val getMatchingExperimentPostsUseCase = GetMatchingExperimentPostsUseCase(experimentPostGateway)

    val fixedClock: Clock = Clock.fixed(Instant.parse("2025-02-02T08:00:00Z"), ZoneId.of("UTC"))
    val createdAt: LocalDateTime = LocalDateTime.now(fixedClock).minusHours(23).plusMinutes(59)

    given("매칭된 실험 공고를 조회할 때") {

        `when`("유효한 실험 공고 목록이 존재하면") {
            val requestTime = LocalDateTime.of(2025, 2, 2, 8, 0, 0)

            val validMember = Member(
                id = "1",
                role = RoleType.RESEARCHER,
                contactEmail = "christer10@naver.com",
                oauthEmail = "christer10@naver.com",
                name = "신수정",
                provider = ProviderType.NAVER,
                status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(fixedClock),
                updatedAt = LocalDateTime.now(fixedClock),
                deletedAt = null
            )

            val experimentPosts = listOf(
                ExperimentPost(
                    id = "1",
                    member = validMember,
                    targetGroup = TargetGroup(
                        id = "1",
                        startAge = 20,
                        endAge = 30,
                        genderType = GenderType.FEMALE,
                        otherCondition = "야뿌이셨던 여성분"
                    ),
                    applyMethod = ApplyMethod(
                        id = "1",
                        content = "구글폼 보고 참여해주세요 :)",
                        formUrl = "google.form.co.kr",
                        phoneNum = "010-5729-7754"
                    ),
                    images = mutableListOf(),
                    title = "야뿌 최고 먹짱 실험자 모집합니다",
                    content = "YAPP에서 가장 치킨 잘 먹는 사람",
                    views = 10,
                    startDate = LocalDate.of(2025, 2, 3),
                    endDate = LocalDate.of(2025, 2, 10),
                    matchType = MatchType.OFFLINE,
                    count = 35,
                    timeRequired = TimeSlot.LESS_30M,
                    leadResearcher = "야뿌 랩실 서버 25기 신수정",
                    place = "이화여자대학교",
                    region = Region.SEOUL,
                    area = Area.SEOUL_ALL,
                    detailedAddress = "ECC B123호",
                    reward = "얍 지각면제권 100장",
                    alarmAgree = true,
                    createdAt = createdAt,
                    updatedAt = createdAt,
                    recruitStatus = true
                )
            )

            val matchingPosts = mapOf(validMember.contactEmail!! to experimentPosts)

            every { experimentPostGateway.findMatchingExperimentPosts() } returns matchingPosts

            then("올바른 결과를 반환해야 한다") {
                val input = GetMatchingExperimentPostsUseCase.Input(requestTime)
                val output = getMatchingExperimentPostsUseCase.execute(input)

                output.matchingPosts shouldBe matchingPosts
            }
        }
    }
})
