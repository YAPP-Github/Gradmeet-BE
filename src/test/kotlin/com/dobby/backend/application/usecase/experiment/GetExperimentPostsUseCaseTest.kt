package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.experiment.GetExperimentPostsUseCase.Input // Input 클래스 임포트
import com.dobby.backend.application.usecase.experiment.GetExperimentPostsUseCase.CustomFilter // CustomFilter 클래스 임포트
import com.dobby.backend.application.usecase.experiment.GetExperimentPostsUseCase.Pagination // CustomFilter 클래스 임포트

import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.ApplyMethod
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.domain.model.experiment.TargetGroup
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enums.*
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime

class GetExperimentPostsUseCaseTest : BehaviorSpec({
    val experimentPostGateway = mockk<ExperimentPostGateway>(relaxed = true)
    val useCase = GetExperimentPostsUseCase(experimentPostGateway)

    given("유효한 필터와 페이지네이션이 주어졌을 때") {
        val customFilter = CustomFilter(
            matchType = MatchType.HYBRID,
            studyTarget = GetExperimentPostsUseCase.StudyTarget(gender = GenderType.FEMALE, age = 24),
            locationTarget = GetExperimentPostsUseCase.LocationTarget(region = Region.SEOUL, areas = listOf(Area.SEODAEMUNGU, Area.MAPOGU)),
            recruitDone = false
        )
        val pagination = Pagination(page = 1, count = 6)
        val input = Input(customFilter, pagination)

        val mockPost = ExperimentPost(
            id = 1L,
            title = "야뿌 피자 먹방 테스트",
            views = 10,
            univName = "야뿌 대학교",
            reward = "스타벅스 1만원권 쿠폰",
            recruitDone = false,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(10),
            alarmAgree = true,
            applyMethod = ApplyMethod(
                id = 1L,
                phoneNum = "123-456-7890",
                formUrl = "https://example.googleform.com",
                content = "구글 폼 참고하여 신청해주세요."
            ),
            region = Region.SEOUL,
            area = Area.SEODAEMUNGU,
            content = "야뿌 테스트 내용",
            count = 10,
            detailedAddress = "앨리스랩",
            images = emptyList(),
            leadResearcher = "야뿌랩실 선행 연구원",
            matchType = MatchType.HYBRID,
            timeRequired = TimeSlot.ABOUT_1H,
            member = Member(
                id = 1L,
                name = "야뿌 연구원",
                role = RoleType.RESEARCHER,
                contactEmail = "researcher@example.com",
                oauthEmail = "researcher@gmail.com",
                provider = ProviderType.GOOGLE,
                status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            targetGroup = TargetGroup(
                id = 0L,
                startAge = 20,
                endAge = 29,
                genderType = GenderType.FEMALE,
                otherCondition = null
            ),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        every {
            experimentPostGateway.findExperimentPostsByCustomFilter(
                input.toDomainFilter(),
                input.toDomainPagination()
            )
        } returns listOf(mockPost)

        `when`("useCase의 execute가 호출되면") {
            val result = useCase.execute(input)

            then("필터된 공고가 반환된다") {
                result.size shouldBe 1
                result.first().postInfo.title shouldBe "야뿌 피자 먹방 테스트"
                result.first().postInfo.univName shouldBe "야뿌 대학교"
                result.first().postInfo.reward shouldBe "스타벅스 1만원권 쿠폰"
                result.first().postInfo.recruitDone shouldBe false
            }
        }
    }

    given("studyTarget이 null인 경우") {
        val customFilter = CustomFilter(
            matchType = MatchType.HYBRID,
            studyTarget = null,
            locationTarget = GetExperimentPostsUseCase.LocationTarget(region = Region.SEOUL, areas = listOf(Area.SEODAEMUNGU)),
            recruitDone = false
        )
        val pagination = Pagination(page = 1, count = 6)
        val input = Input(customFilter, pagination)

        val mockPost = ExperimentPost(
            id = 1L,
            title = "Study Target Null Test",
            views = 10,
            univName = "Test University",
            reward = "Test Reward",
            recruitDone = false,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(10),
            alarmAgree = true,
            applyMethod = ApplyMethod(
                id = 1L,
                phoneNum = "123-456-7890",
                formUrl = "https://example.googleform.com",
                content = "구글 폼 참고하여 신청해주세요."
            ),
            region = Region.SEOUL,
            area = Area.SEODAEMUNGU,
            content = "Test content",
            count = 10,
            detailedAddress = "Test Address",
            images = emptyList(),
            leadResearcher = "Test Researcher",
            matchType = MatchType.HYBRID,
            timeRequired = TimeSlot.ABOUT_1H,
            member = Member(
                id = 1L,
                name = "Test Member",
                role = RoleType.RESEARCHER,
                contactEmail = "researcher@example.com",
                oauthEmail = "researcher@gmail.com",
                provider = ProviderType.GOOGLE,
                status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            targetGroup = TargetGroup(
                id = 0L,
                startAge = 20,
                endAge = 29,
                genderType = GenderType.FEMALE,
                otherCondition = null
            ),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        every {
            experimentPostGateway.findExperimentPostsByCustomFilter(
                input.toDomainFilter(),
                input.toDomainPagination()
            )
        } returns listOf(mockPost)

        `when`("execute가 호출되면") {
            val result = useCase.execute(input)

            then("studyTarget이 null인 경우에도 필터링된 공고가 반환된다") {
                result.size shouldBe 1
                result.first().postInfo.title shouldBe "Study Target Null Test"
            }
        }
    }

    given("locationTarget의 areas가 null인 경우") {
        val customFilter = CustomFilter(
            matchType = MatchType.HYBRID,
            studyTarget = GetExperimentPostsUseCase.StudyTarget(gender = GenderType.FEMALE, age = 24),
            locationTarget = GetExperimentPostsUseCase.LocationTarget(region = Region.SEOUL, areas = null),
            recruitDone = false
        )
        val pagination = Pagination(page = 1, count = 6)
        val input = Input(customFilter, pagination)

        val mockPost = ExperimentPost(
            id = 1L,
            title = "Location Target Null Test",
            views = 10,
            univName = "Test University",
            reward = "Test Reward",
            recruitDone = false,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(10),
            alarmAgree = true,
            applyMethod = ApplyMethod(
                id = 1L,
                phoneNum = "123-456-7890",
                formUrl = "https://example.googleform.com",
                content = "구글 폼 참고하여 신청해주세요."
            ),
            region = Region.SEOUL,
            area = Area.SEODAEMUNGU,
            content = "Test content",
            count = 10,
            detailedAddress = "Test Address",
            images = emptyList(),
            leadResearcher = "Test Researcher",
            matchType = MatchType.HYBRID,
            timeRequired = TimeSlot.ABOUT_1H,
            member = Member(
                id = 1L,
                name = "Test Member",
                role = RoleType.RESEARCHER,
                contactEmail = "researcher@example.com",
                oauthEmail = "researcher@gmail.com",
                provider = ProviderType.GOOGLE,
                status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            targetGroup = TargetGroup(
                id = 0L,
                startAge = 20,
                endAge = 29,
                genderType = GenderType.FEMALE,
                otherCondition = null
            ),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        every {
            experimentPostGateway.findExperimentPostsByCustomFilter(
                input.toDomainFilter(),
                input.toDomainPagination()
            )
        } returns listOf(mockPost)

        `when`("execute가 호출되면") {
            val result = useCase.execute(input)

            then("locationTarget의 areas가 null인 경우에도 필터링된 공고가 반환된다") {
                result.size shouldBe 1
                result.first().postInfo.title shouldBe "Location Target Null Test"
            }
        }
    }
    given("필터에 맞는 공고가 없을 경우") {
        val customFilter = CustomFilter(
            matchType = MatchType.HYBRID,
            studyTarget = GetExperimentPostsUseCase.StudyTarget(gender = GenderType.FEMALE, age = 30),
            locationTarget = GetExperimentPostsUseCase.LocationTarget(region = Region.GYEONGGI, areas = listOf(Area.GWANGMYEONGSI)),
            recruitDone = false
        )
        val pagination = Pagination(page = 1, count = 6)
        val input = Input(customFilter, pagination)

        every {
            experimentPostGateway.findExperimentPostsByCustomFilter(
                input.toDomainFilter(),
                input.toDomainPagination()
            )
        } returns emptyList()

        `when`("execute가 호출되면") {
            val result = useCase.execute(input)

            then("결과가 비어있음") {
                result shouldBe emptyList()
            }
        }
    }
})

