package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.experiment.CreateExperimentPostUseCase
import com.dobby.backend.application.usecase.experiment.GetExperimentPostCountsByRegionUseCase
import com.dobby.backend.config.RedisTestContainer
import com.dobby.backend.domain.gateway.CacheGateway
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import com.dobby.backend.infrastructure.database.entity.enums.experiment.RecruitStatus
import com.dobby.backend.infrastructure.database.entity.enums.experiment.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enums.member.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.member.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enums.member.ProviderType
import com.dobby.backend.infrastructure.database.entity.enums.member.RoleType
import com.dobby.backend.infrastructure.database.entity.member.MemberEntity
import com.dobby.backend.infrastructure.database.repository.MemberRepository
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldBeNull
import io.mockk.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
class ExperimentPostServiceTest @Autowired constructor(
    private val experimentPostService: ExperimentPostService,
    private val memberRepository: MemberRepository,
    private val cacheGateway: CacheGateway,
    private val objectMapper: ObjectMapper
) : BehaviorSpec({

    val createExperimentPostUseCase = mockk<CreateExperimentPostUseCase>()
    lateinit var memberId: String

    listeners(RedisTestContainer)

    beforeSpec {
        clearAllMocks()

        val savedMember = memberRepository.save(
            MemberEntity(
                id = "1",
                oauthEmail = "test@gmail.com",
                contactEmail = "test@gmail.com",
                provider = ProviderType.GOOGLE,
                status = MemberStatus.ACTIVE,
                role = RoleType.RESEARCHER,
                name = "테스트",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                deletedAt = null
            )
        )
        memberId = savedMember.id
    }

    given("Redis 캐시에 지역별 실험 공고 개수를 저장할 때") {
        val input = GetExperimentPostCountsByRegionUseCase.Input(region = null, recruitStatus = RecruitStatus.ALL)
        val output = GetExperimentPostCountsByRegionUseCase.Output(total = 100, area = emptyList())

        val cacheKey = "experimentPostCounts:ALL"

        `when`("캐시에 데이터를 저장하고 조회하면") {
            cacheGateway.set(cacheKey, objectMapper.writeValueAsString(output))
            val cachedData = cacheGateway.get(cacheKey)

            then("저장된 데이터가 올바르게 반환되어야 한다") {
                cachedData shouldBe objectMapper.writeValueAsString(output)
            }
        }
    }

    given("새로운 실험 공고를 생성할 때") {
        val cacheKey = "experimentPostCounts:ALL"
        cacheGateway.set(cacheKey, objectMapper.writeValueAsString(GetExperimentPostCountsByRegionUseCase.Output(100, emptyList())))

        val mockInput = mockk<CreateExperimentPostUseCase.Input>(relaxed = true)

        every { mockInput.memberId } returns memberId
        every { mockInput.leadResearcher } returns "야뿌 연구 리더"
        every { mockInput.place } returns "야뿌대학교"
        every { mockInput.region } returns Region.SEOUL
        every { mockInput.area } returns Area.GWANAKGU
        every { mockInput.detailedAddress } returns "야뿌 연구소"
        every { mockInput.reward } returns "Gift Card"
        every { mockInput.title } returns "새로운 실험"
        every { mockInput.content } returns "참여해 참여해"
        every { mockInput.alarmAgree } returns true
        every { mockInput.startDate } returns LocalDate.of(2024, 6, 1)
        every { mockInput.endDate } returns LocalDate.of(2024, 6, 30)
        every { mockInput.matchType } returns MatchType.OFFLINE
        every { mockInput.count } returns 3
        every { mockInput.timeRequired } returns TimeSlot.ABOUT_1H

        val mockTargetGroupInfo = mockk<CreateExperimentPostUseCase.TargetGroupInfo>(relaxed = true)
        every { mockTargetGroupInfo.startAge } returns 20
        every { mockTargetGroupInfo.endAge } returns 30
        every { mockTargetGroupInfo.genderType } returns GenderType.MALE
        every { mockTargetGroupInfo.otherCondition } returns "Healthy"

        every { mockInput.targetGroupInfo } returns mockTargetGroupInfo

        val mockApplyMethodInfo = mockk<CreateExperimentPostUseCase.ApplyMethodInfo>(relaxed = true)
        every { mockApplyMethodInfo.content } returns "이메일로 연락주세요"
        every { mockApplyMethodInfo.formUrl } returns "https://test.com/apply"
        every { mockApplyMethodInfo.phoneNum } returns "010-1234-5678"

        every { mockInput.applyMethodInfo } returns mockApplyMethodInfo

        val mockImageListInfo = mockk<CreateExperimentPostUseCase.ImageListInfo>(relaxed = true)
        every { mockImageListInfo.images } returns listOf("https://test.com/image1.jpg", "https://test.com/image2.jpg")
        every { mockInput.imageListInfo } returns mockImageListInfo

        every { createExperimentPostUseCase.execute(any()) } returns mockk()

        `when`("게시글을 생성하면") {
            experimentPostService.createNewExperimentPost(mockInput)

            then("캐시가 삭제되어야 한다") {
                val cachedData = cacheGateway.get(cacheKey)
                cachedData.shouldBeNull()
            }
        }
    }
})
