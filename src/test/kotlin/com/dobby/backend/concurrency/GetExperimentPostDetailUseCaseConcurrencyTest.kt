package com.dobby.backend.concurrency

import com.dobby.backend.application.usecase.experiment.GetExperimentPostDetailUseCase
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.ApplyMethod
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.domain.model.experiment.TargetGroup
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import com.dobby.backend.infrastructure.database.entity.enums.experiment.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enums.member.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.member.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enums.member.ProviderType
import com.dobby.backend.infrastructure.database.entity.enums.member.RoleType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.util.ReflectionTestUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

@ExtendWith(SpringExtension::class)
@SpringBootTest
@ActiveProfiles("test")
class GetExperimentPostDetailUseCaseConcurrencyTest {

    @Autowired
    private lateinit var applicationContext: ApplicationContext
    private lateinit var getExperimentPostDetailUseCase: GetExperimentPostDetailUseCase
    private lateinit var experimentPostGateway: ExperimentPostGateway

    @BeforeEach
    fun setUp() {
        experimentPostGateway = mock(ExperimentPostGateway::class.java)
        getExperimentPostDetailUseCase = applicationContext.getBean(GetExperimentPostDetailUseCase::class.java)
        ReflectionTestUtils.setField(getExperimentPostDetailUseCase, "experimentPostGateway", experimentPostGateway)
    }

    @Test
    fun `동시에 여러 요청이 들어와도 조회수가 정확히 1 증가해야 한다`() = runBlocking {
        // given
        val experimentPostId = "123"
        val initialViews = 100

        val member = Member(
            id = "1",
            oauthEmail = "user@example.com",
            contactEmail = "user@example.com",
            provider = ProviderType.NAVER,
            role = RoleType.PARTICIPANT,
            name = "테스터",
            status = MemberStatus.ACTIVE,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = null
        )

        val targetGroup = TargetGroup(
            id = "tg-1",
            startAge = 18,
            endAge = 30,
            genderType = GenderType.ALL,
            otherCondition = "조건 없음"
        )

        val applyMethod = ApplyMethod(
            id = "am-1",
            phoneNum = "010-1111-2222",
            formUrl = "http://apply.example.com",
            content = "지원 방법 설명"
        )

        val experimentPost = ExperimentPost(
            id = experimentPostId,
            title = "동시성 테스트 포스트",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            member = member,
            views = initialViews,
            recruitStatus = false,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(7),
            leadResearcher = "리드 연구원",
            matchType = MatchType.ALL,
            reward = "상품권",
            count = 10,
            timeRequired = TimeSlot.ABOUT_1H,
            targetGroup = targetGroup,
            applyMethod = applyMethod,
            region = Region.SEOUL,
            area = Area.GWANGJINGU,
            place = "테스트 장소",
            detailedAddress = "테스트 상세 주소",
            content = "실험 내용",
            alarmAgree = false,
            images = mutableListOf()
        )

        `when`(experimentPostGateway.findById(experimentPostId)).thenReturn(experimentPost)

        doAnswer { invocation ->
            invocation.arguments[0] as ExperimentPost
            null
        }.`when`(experimentPostGateway).save(any())

        // 동시에 요청할 횟수
        val numberOfRequests = 10
        val latch = CountDownLatch(numberOfRequests)
        val successCounter = AtomicInteger(0)

        // when
        (1..numberOfRequests).forEach { index ->
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    getExperimentPostDetailUseCase.process(
                        GetExperimentPostDetailUseCase.Input(
                            experimentPostId = experimentPostId,
                            memberId = "member-$index"
                        )
                    )
                    successCounter.incrementAndGet()
                } catch (e: Exception) {
                    println("요청 $index 에서 예외 발생: ${e.message}")
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        // then
        verify(experimentPostGateway, times(1)).save(experimentPost)
        assertEquals(initialViews + 1, experimentPost.views, "조회수가 단 1회 증가해야 합니다.")
    }
}
