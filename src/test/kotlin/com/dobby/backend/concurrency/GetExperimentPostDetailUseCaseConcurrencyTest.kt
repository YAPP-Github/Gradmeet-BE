package com.dobby.backend.concurrency

import com.dobby.backend.application.usecase.experiment.GetExperimentPostDetailUseCase
import com.dobby.backend.config.RedisTestContainer
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
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
@ActiveProfiles("test")
class GetExperimentPostDetailUseCaseConcurrencyTest(
    private val getExperimentPostDetailUseCase: GetExperimentPostDetailUseCase,
    private val experimentPostGateway: ExperimentPostGateway
) : BehaviorSpec({

    listeners(RedisTestContainer)

    Given("동시에 여러 요청이 들어오는 경우") {
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

        whenever(experimentPostGateway.findById(experimentPostId))
            .thenReturn(experimentPost)

        doAnswer { invocation ->
            val savedPost = invocation.arguments[0] as ExperimentPost
            experimentPost.views = savedPost.views
            null
        }.whenever(experimentPostGateway).save(any())

        val numberOfRequests = 10
        val latch = CountDownLatch(numberOfRequests)
        val successCounter = AtomicInteger(0)

        `when`("동시에 여러 요청이 들어오면") {
            runBlocking {
                coroutineScope {
                    repeat(numberOfRequests) { index ->
                        launch(Dispatchers.IO) {
                            try {
                                getExperimentPostDetailUseCase.execute(
                                    GetExperimentPostDetailUseCase.Input(
                                        experimentPostId = experimentPostId,
                                        memberId = "member-$index"
                                    )
                                )
                                successCounter.incrementAndGet()
                            } catch (_: Exception) {
                                // 예외 무시
                            } finally {
                                latch.countDown()
                            }
                        }
                    }
                }
            }

            latch.await()

            Then("조회수는 단 1회 증가해야 한다") {
                verify(experimentPostGateway)
                    .save(any())
                experimentPost.views shouldBe (initialViews + 1)
            }
        }
    }
}) {
    @TestConfiguration
    class TestConfig {
        @Bean
        fun experimentPostGateway(): ExperimentPostGateway = mock()
    }
}
