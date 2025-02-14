package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.domain.exception.ExperimentPostImageSizeException
import com.dobby.backend.domain.exception.ExperimentPostInvalidOnlineRequestException
import com.dobby.backend.domain.exception.PermissionDeniedException
import com.dobby.backend.domain.IdGenerator
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enums.*
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import com.dobby.backend.infrastructure.database.entity.enums.experiment.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enums.member.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.member.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enums.member.ProviderType
import com.dobby.backend.infrastructure.database.entity.enums.member.RoleType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.LocalDateTime

class CreateExperimentPostUseCaseTest: BehaviorSpec ({
    val experimentPostGateway: ExperimentPostGateway = mockk(relaxed = true)
    val memberGateway: MemberGateway = mockk()
    val idGenerator: IdGenerator = mockk()

    val createExperimentPostUseCase = CreateExperimentPostUseCase(experimentPostGateway, memberGateway, idGenerator)

    given("유효한 입력값이 주어졌을 때") {
        val validMember = Member(
            id = "1",
            role = RoleType.RESEARCHER,
            contactEmail = "christer10@naver.com",
            oauthEmail = "christer10@naver.com",
            name = "신수정",
            provider = ProviderType.NAVER,
            status = MemberStatus.ACTIVE,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = null
        )

        val validInput = CreateExperimentPostUseCase.Input(
            memberId = "1",
            targetGroupInfo = CreateExperimentPostUseCase.TargetGroupInfo(
                startAge = 20, endAge = 30, genderType = GenderType.MALE, otherCondition = "야뿌이셨던 남성분"
            ),
            applyMethodInfo = CreateExperimentPostUseCase.ApplyMethodInfo(
                content = "구글폼 보고 참여해주세요 :)",
                formUrl = "google.form.co.kr",
                phoneNum = "010-5729-7754"
            ),
            imageListInfo = CreateExperimentPostUseCase.ImageListInfo(
                images = listOf("이미지1", "이미지2", "이미지3")
            ),
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
            title = "야뿌 최고 먹짱 실험자 모집합니다",
            content = "YAPP에서 가장 치킨 잘 먹는 사람",
            alarmAgree = true
        )

        every { memberGateway.getById(validInput.memberId) } returns validMember
        every { idGenerator.generateId() } returns "1"
        every { experimentPostGateway.save(any()) } answers {
            val capturedExperimentPost = firstArg<ExperimentPost>()
            capturedExperimentPost.copy(id = "0") // DB에 저장된 ID 시뮬레이션
        }

        `when`("유즈케이스를 실행하면") {
            val result = createExperimentPostUseCase.execute(validInput)

            then("정상적으로 실험 게시글이 생성되어야 한다") {
                result.postInfo.postId shouldBe "0"
                result.postInfo.title shouldBe validInput.title
                result.postInfo.place shouldBe validInput.place
                result.postInfo.reward shouldBe validInput.reward
            }
        }
    }

    given("연구자가 아닌 사용자가 게시글을 생성하려고 하면") {
        val invalidMember =  Member(
            id = "0",
            role = RoleType.PARTICIPANT,
            contactEmail = "christer10@naver.com",
            oauthEmail = "christer10@naver.com",
            name = "신수정 실험자",
            provider = ProviderType.NAVER,
            status = MemberStatus.ACTIVE,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = null
        )

        val invalidInput = CreateExperimentPostUseCase.Input(
            memberId = "0",
            targetGroupInfo = CreateExperimentPostUseCase.TargetGroupInfo(
                startAge = 20, endAge = 30, genderType = GenderType.MALE, otherCondition = "야뿌이셨던 남성분"
            ),
            applyMethodInfo = CreateExperimentPostUseCase.ApplyMethodInfo(
                content = "구글폼 보고 참여해주세요 :)",
                formUrl = "google.form.co.kr",
                phoneNum = "010-5729-7754"
            ),
            imageListInfo = CreateExperimentPostUseCase.ImageListInfo(
                images = listOf("이미지1", "이미지2", "이미지3")
            ),
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
            title = "야뿌 최고 먹짱 실험자 모집합니다",
            content = "YAPP에서 가장 치킨 잘 먹는 사람",
            alarmAgree = true
        )

        every { memberGateway.getById(invalidMember.id) } returns invalidMember

        `when`("유즈케이스를 실행하면") {
            then("PermissionDeniedException 예외가 발생해야 한다") {
                shouldThrow<PermissionDeniedException> {
                    createExperimentPostUseCase.execute(invalidInput)
                }
            }
        }
    }

    given("이미지가 3장을 초과하면") {
        val validMember = Member(
            id = "3",
            role = RoleType.RESEARCHER,
            contactEmail = "christer10@naver.com",
            oauthEmail = "christer10@naver.com",
            name = "신수정",
            provider = ProviderType.NAVER,
            status = MemberStatus.ACTIVE,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = null
        )

        val invalidInput = CreateExperimentPostUseCase.Input(
            memberId = "3",
            targetGroupInfo = CreateExperimentPostUseCase.TargetGroupInfo(
                startAge = 20, endAge = 30, genderType = GenderType.MALE, otherCondition = "야뿌이셨던 남성분"
            ),
            applyMethodInfo = CreateExperimentPostUseCase.ApplyMethodInfo(
                content = "구글폼 보고 참여해주세요 :)",
                formUrl = "google.form.co.kr",
                phoneNum = "010-5729-7754"
            ),
            imageListInfo = CreateExperimentPostUseCase.ImageListInfo(
                images = listOf("이미지1", "이미지2", "이미지3", "이미지4")
            ),
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
            title = "야뿌 최고 먹짱 실험자 모집합니다",
            content = "YAPP에서 가장 치킨 잘 먹는 사람",
            alarmAgree = true
        )


        every { memberGateway.getById(invalidInput.memberId) } returns validMember
        every { idGenerator.generateId() } returns "1"

        `when`("유즈케이스를 실행하면") {
            then("ExperimentPostImageSizeException 예외가 발생해야 한다") {
                shouldThrow<ExperimentPostImageSizeException> {
                    createExperimentPostUseCase.execute(invalidInput)
                }
            }
        }
    }

    given("온라인 매칭인데 지역 정보가 입력된 경우") {
        val validMember = Member(
            id = "3",
            role = RoleType.RESEARCHER,
            contactEmail = "christer10@naver.com",
            oauthEmail = "christer10@naver.com",
            name = "신수정",
            provider = ProviderType.NAVER,
            status = MemberStatus.ACTIVE,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = null
        )

        val invalidInput = CreateExperimentPostUseCase.Input(
            memberId = "3",
            targetGroupInfo = CreateExperimentPostUseCase.TargetGroupInfo(
                startAge = 20, endAge = 30, genderType = GenderType.MALE, otherCondition = "야뿌이셨던 남성분"
            ),
            applyMethodInfo = CreateExperimentPostUseCase.ApplyMethodInfo(
                content = "구글폼 보고 참여해주세요 :)",
                formUrl = "google.form.co.kr",
                phoneNum = "010-5729-7754"
            ),
            imageListInfo = CreateExperimentPostUseCase.ImageListInfo(
                images = listOf("이미지1", "이미지2", "이미지3")
            ),
            startDate = LocalDate.of(2025, 2, 3),
            endDate = LocalDate.of(2025, 2, 10),
            matchType = MatchType.ONLINE, //온라인 매칭설정
            count = 35,
            timeRequired = TimeSlot.LESS_30M,
            leadResearcher = "야뿌 랩실 서버 25기 신수정",
            place = "이화여자대학교",
            region = Region.SEOUL,
            area = Area.SEOUL_ALL,
            detailedAddress = "ECC B123호",
            reward = "얍 지각면제권 100장",
            title = "야뿌 최고 먹짱 실험자 모집합니다",
            content = "YAPP에서 가장 치킨 잘 먹는 사람",
            alarmAgree = true
        )

        every { memberGateway.getById(invalidInput.memberId) } returns validMember
        every { idGenerator.generateId() } returns "1"

        `when`("유즈케이스를 실행하면") {
            then("ExperimentPostInvalidOnlineRequestException 예외가 발생해야 한다") {
                shouldThrow<ExperimentPostInvalidOnlineRequestException> {
                    createExperimentPostUseCase.execute(invalidInput)
                }
            }
        }
    }

})
