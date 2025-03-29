package com.dobby.usecase.experiment

import com.dobby.enums.MatchType
import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import com.dobby.enums.experiment.RecruitStatus
import com.dobby.enums.experiment.TimeSlot
import com.dobby.enums.member.GenderType
import com.dobby.enums.member.MemberStatus
import com.dobby.enums.member.ProviderType
import com.dobby.enums.member.RoleType
import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.model.experiment.*
import com.dobby.model.member.Member
import com.dobby.usecase.experiment.GetExperimentPostsUseCase.CustomFilterInput
import com.dobby.usecase.experiment.GetExperimentPostsUseCase.Input
import com.dobby.usecase.experiment.GetExperimentPostsUseCase.LocationTargetInput
import com.dobby.usecase.experiment.GetExperimentPostsUseCase.PaginationInput
import com.dobby.usecase.experiment.GetExperimentPostsUseCase.StudyTargetInput
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
        val customFilter = CustomFilterInput(
            matchType = MatchType.ALL,
            studyTarget = StudyTargetInput(gender = GenderType.FEMALE, age = 24),
            locationTarget = LocationTargetInput(region = Region.SEOUL, areas = listOf(Area.SEODAEMUNGU, Area.MAPOGU)),
            recruitStatus = RecruitStatus.ALL
        )
        val pagination = PaginationInput(page = 1, count = 6, order = "DESC")
        val input = Input(customFilter, pagination)

        val mockPost = ExperimentPost(
            id = "1",
            title = "야뿌 피자 먹방 테스트",
            views = 10,
            place = "야뿌 대학교",
            reward = "스타벅스 1만원권 쿠폰",
            recruitStatus = false,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(10),
            alarmAgree = true,
            applyMethod = ApplyMethod(
                id = "1",
                phoneNum = "123-456-7890",
                formUrl = "https://example.googleform.com",
                content = "구글 폼 참고하여 신청해주세요."
            ),
            region = Region.SEOUL,
            area = Area.SEODAEMUNGU,
            content = "야뿌 테스트 내용",
            count = 10,
            detailedAddress = "앨리스랩",
            images = mutableListOf(),
            leadResearcher = "야뿌랩실 선행 연구원",
            matchType = MatchType.ALL,
            timeRequired = TimeSlot.ABOUT_1H,
            member = Member(
                id = "1",
                name = "야뿌 연구원",
                role = RoleType.RESEARCHER,
                contactEmail = "researcher@example.com",
                oauthEmail = "researcher@gmail.com",
                provider = ProviderType.GOOGLE,
                status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                deletedAt = null
            ),
            targetGroup = TargetGroup(
                id = "0",
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
                CustomFilter.newCustomFilter(
                    customFilter.matchType,
                    customFilter.studyTarget?.let { StudyTarget(it.gender, it.age) },
                    customFilter.locationTarget?.let { LocationTarget(it.region, it.areas) },
                    customFilter.recruitStatus
                ),
                pagination.page,
                pagination.count,
                pagination.order
            )
        } returns listOf(mockPost)

        `when`("useCase의 execute가 호출되면") {
            val result = useCase.execute(input)

            then("필터된 공고가 반환된다") {
                result.size shouldBe 1
                result.first().postInfo.title shouldBe "야뿌 피자 먹방 테스트"
                result.first().postInfo.place shouldBe "야뿌 대학교"
                result.first().postInfo.reward shouldBe "스타벅스 1만원권 쿠폰"
                result.first().postInfo.recruitStatus shouldBe false
            }
        }
    }

    given("studyTarget이 null인 경우") {
        val customFilter = CustomFilterInput(
            matchType = MatchType.ALL,
            studyTarget = null,
            locationTarget = LocationTargetInput(region = Region.SEOUL, areas = listOf(Area.SEODAEMUNGU)),
            recruitStatus = RecruitStatus.ALL
        )
        val pagination = PaginationInput(page = 1, count = 6)
        val input = Input(customFilter, pagination)

        val mockPost = ExperimentPost(
            id = "1",
            title = "Study Target Null Test",
            views = 10,
            place = "Test University",
            reward = "Test Reward",
            recruitStatus = false,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(10),
            alarmAgree = true,
            applyMethod = ApplyMethod(
                id = "1",
                phoneNum = "123-456-7890",
                formUrl = "https://example.googleform.com",
                content = "구글 폼 참고하여 신청해주세요."
            ),
            region = Region.SEOUL,
            area = Area.SEODAEMUNGU,
            content = "Test content",
            count = 10,
            detailedAddress = "Test Address",
            images = mutableListOf(),
            leadResearcher = "Test Researcher",
            matchType = MatchType.ALL,
            timeRequired = TimeSlot.ABOUT_1H,
            member = Member(
                id = "1",
                name = "Test Member",
                role = RoleType.RESEARCHER,
                contactEmail = "researcher@example.com",
                oauthEmail = "researcher@gmail.com",
                provider = ProviderType.GOOGLE,
                status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                deletedAt = null
            ),
            targetGroup = TargetGroup(
                id = "0",
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
                CustomFilter.newCustomFilter(
                    customFilter.matchType,
                    customFilter.studyTarget?.let { StudyTarget(it.gender, it.age) },
                    customFilter.locationTarget?.let { LocationTarget(it.region, it.areas) },
                    customFilter.recruitStatus
                ),
                pagination.page,
                pagination.count,
                pagination.order
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
        val customFilter = CustomFilterInput(
            matchType = MatchType.ALL,
            studyTarget = StudyTargetInput(gender = GenderType.FEMALE, age = 24),
            locationTarget = LocationTargetInput(region = Region.SEOUL, areas = null),
            recruitStatus = RecruitStatus.ALL
        )
        val pagination = PaginationInput(page = 1, count = 6)
        val input = Input(customFilter, pagination)

        val mockPost = ExperimentPost(
            id = "1",
            title = "Location Target Null Test",
            views = 10,
            place = "Test University",
            reward = "Test Reward",
            recruitStatus = false,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(10),
            alarmAgree = true,
            applyMethod = ApplyMethod(
                id = "1",
                phoneNum = "123-456-7890",
                formUrl = "https://example.googleform.com",
                content = "구글 폼 참고하여 신청해주세요."
            ),
            region = Region.SEOUL,
            area = Area.SEODAEMUNGU,
            content = "Test content",
            count = 10,
            detailedAddress = "Test Address",
            images = mutableListOf(),
            leadResearcher = "Test Researcher",
            matchType = MatchType.ALL,
            timeRequired = TimeSlot.ABOUT_1H,
            member = Member(
                id = "1",
                name = "Test Member",
                role = RoleType.RESEARCHER,
                contactEmail = "researcher@example.com",
                oauthEmail = "researcher@gmail.com",
                provider = ProviderType.GOOGLE,
                status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                deletedAt = null
            ),
            targetGroup = TargetGroup(
                id = "0",
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
                CustomFilter.newCustomFilter(
                    customFilter.matchType,
                    customFilter.studyTarget?.let { StudyTarget(it.gender, it.age) },
                    customFilter.locationTarget?.let { LocationTarget(it.region, it.areas) },
                    customFilter.recruitStatus
                ),
                pagination.page,
                pagination.count,
                pagination.order
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
        val customFilter = CustomFilterInput(
            matchType = MatchType.ALL,
            studyTarget = StudyTargetInput(gender = GenderType.FEMALE, age = 30),
            locationTarget = LocationTargetInput(region = Region.GYEONGGI, areas = listOf(Area.GWANGMYEONGSI)),
            recruitStatus = RecruitStatus.ALL
        )
        val pagination = PaginationInput(page = 1, count = 6)
        val input = Input(customFilter, pagination)

        val mockPost = ExperimentPost(
            id = "1",
            title = "Location Target Null Test",
            views = 10,
            place = "Test University",
            reward = "Test Reward",
            recruitStatus = false,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(10),
            alarmAgree = true,
            applyMethod = ApplyMethod(
                id = "1",
                phoneNum = "123-456-7890",
                formUrl = "https://example.googleform.com",
                content = "구글 폼 참고하여 신청해주세요."
            ),
            region = Region.SEOUL,
            area = Area.SEODAEMUNGU,
            content = "Test content",
            count = 10,
            detailedAddress = "Test Address",
            images = mutableListOf(),
            leadResearcher = "Test Researcher",
            matchType = MatchType.ALL,
            timeRequired = TimeSlot.ABOUT_1H,
            member = Member(
                id = "1",
                name = "Test Member",
                role = RoleType.RESEARCHER,
                contactEmail = "researcher@example.com",
                oauthEmail = "researcher@gmail.com",
                provider = ProviderType.GOOGLE,
                status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                deletedAt = null
            ),
            targetGroup = TargetGroup(
                id = "0",
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
                CustomFilter.newCustomFilter(
                    customFilter.matchType,
                    customFilter.studyTarget?.let { StudyTarget(it.gender, it.age) },
                    customFilter.locationTarget?.let { LocationTarget(it.region, it.areas) },
                    customFilter.recruitStatus
                ),
                pagination.page,
                pagination.count,
                pagination.order
            )
        } returns mutableListOf()

        `when`("execute가 호출되면") {
            val result = useCase.execute(input)

            then("결과가 비어있음") {
                result shouldBe mutableListOf()
            }
        }
    }

    given("필터가 없는 경우") {
        val customFilter = CustomFilterInput(
            matchType = MatchType.ALL,
            studyTarget = null,
            locationTarget = LocationTargetInput(region = Region.SEOUL, areas = listOf(Area.SEODAEMUNGU)),
            recruitStatus = RecruitStatus.ALL
        )
        val pagination = PaginationInput(page = 1, count = 6)
        val input = Input(customFilter, pagination)

        val mockPost = ExperimentPost(
            id = "1",
            title = "야뿌 피자 먹방 테스트",
            views = 10,
            place = "Test University",
            reward = "Test Reward",
            recruitStatus = false,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(10),
            alarmAgree = true,
            applyMethod = ApplyMethod(
                id = "1",
                phoneNum = "123-456-7890",
                formUrl = "https://example.googleform.com",
                content = "구글 폼 참고하여 신청해주세요."
            ),
            region = Region.SEOUL,
            area = Area.SEODAEMUNGU,
            content = "Test content",
            count = 10,
            detailedAddress = "Test Address",
            images = mutableListOf(),
            leadResearcher = "Test Researcher",
            matchType = MatchType.ALL,
            timeRequired = TimeSlot.ABOUT_1H,
            member = Member(
                id = "1",
                name = "Test Member",
                role = RoleType.RESEARCHER,
                contactEmail = "researcher@example.com",
                oauthEmail = "researcher@gmail.com",
                provider = ProviderType.GOOGLE,
                status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                deletedAt = null
            ),
            targetGroup = TargetGroup(
                id = "0",
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
                CustomFilter.newCustomFilter(
                    customFilter.matchType,
                    customFilter.studyTarget?.let { StudyTarget(it.gender, it.age) },
                    customFilter.locationTarget?.let { LocationTarget(it.region, it.areas) },
                    customFilter.recruitStatus
                ),
                pagination.page,
                pagination.count,
                pagination.order
            )
        } returns listOf(mockPost)

        `when`("필터가 없으면 모든 공고가 반환된다") {
            val result = useCase.execute(input)

            then("모든 공고가 반환된다") {
                result.size shouldBe 1
                result.first().postInfo.title shouldBe "야뿌 피자 먹방 테스트"
            }
        }
    }
})
