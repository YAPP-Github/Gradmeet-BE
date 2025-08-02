package com.dobby.concurrency

import com.dobby.enums.MatchType
import com.dobby.enums.experiment.TimeSlot
import com.dobby.enums.member.GenderType
import com.dobby.enums.member.MemberStatus
import com.dobby.enums.member.ProviderType
import com.dobby.enums.member.RoleType
import com.dobby.gateway.OpenAiGateway
import com.dobby.gateway.UsageLimitGateway
import com.dobby.gateway.experiment.ExperimentPostKeywordsLogGateway
import com.dobby.gateway.member.MemberGateway
import com.dobby.model.experiment.keyword.ApplyMethodKeyword
import com.dobby.model.experiment.keyword.ExperimentPostKeywords
import com.dobby.model.experiment.keyword.TargetGroupKeyword
import com.dobby.model.member.Member
import com.dobby.usecase.experiment.ExtractExperimentPostKeywordsUseCase
import com.dobby.util.IdGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicInteger

@ExtendWith(MockitoExtension::class)
@SpringBootTest
@ActiveProfiles("test")
class ExtractExperimentPostKeywordsConcurrencyTest {

    companion object {
        private const val THREAD_COUNT = 5
        private const val DAILY_LIMIT = 2
    }

    private lateinit var useCase: ExtractExperimentPostKeywordsUseCase

    private val openAiGateway = mock<OpenAiGateway>()
    private val experimentPostKeywordsGateway = mock<ExperimentPostKeywordsLogGateway>()
    private val memberGateway = mock<MemberGateway>()
    private val usageLimitGateway = mock<UsageLimitGateway>()
    private val idGenerator = object {
        private var id = 0L
        fun generateId() = (++id).toString()
    }

    private val memberId = "test-user"
    private val text = "이 실험은 집중력과 관련된 실험입니다."

    @BeforeEach
    fun setup() {
        whenever(memberGateway.getById(any())).thenReturn(
            Member(
                memberId, "테스트 유저", "dlawltn123@naver.com", "dlawltn456@naver.com", ProviderType.NAVER,
                MemberStatus.ACTIVE, RoleType.RESEARCHER, LocalDateTime.now(), LocalDateTime.now(), null
            )
        )

        val sampleKeywords = ExperimentPostKeywords(
            targetGroup = TargetGroupKeyword(
                startAge = 18,
                endAge = 30,
                genderType = GenderType.ALL,
                otherCondition = "건강한 대학생 및 직장인 대상"
            ),
            applyMethod = ApplyMethodKeyword(
                content = "온라인 설문 작성 및 전화 인터뷰 가능",
                isFormUrl = true,
                formUrl = "https://example.com/survey-form",
                isPhoneNum = true,
                phoneNum = "010-1234-5678"
            ),
            matchType = MatchType.ALL,
            reward = "실험 참여 시 2만원 상당 상품권 지급",
            count = 50,
            timeRequired = TimeSlot.ABOUT_1H
        )
        whenever(openAiGateway.extractKeywords(any())).thenReturn(sampleKeywords)

        val usageCount = AtomicInteger(0)

        whenever(experimentPostKeywordsGateway.save(any())).thenAnswer {
            usageCount.incrementAndGet()
            null
        }

        val callCount = AtomicInteger(0)
        whenever(usageLimitGateway.incrementAndCheckLimit(any(), any())).thenAnswer {
            val dailyLimit = it.getArgument<Int>(1)
            val current = callCount.incrementAndGet()
            current <= dailyLimit
        }

        useCase = ExtractExperimentPostKeywordsUseCase(
            openAiGateway,
            experimentPostKeywordsGateway,
            memberGateway,
            usageLimitGateway,
            idGenerator = object : IdGenerator {
                override fun generateId(): String = idGenerator.generateId()
            }
        )
    }

//    @Test
//    fun `동시에 여러 요청 시 최대 2번까지만 성공하고 나머지는 제한 예외가 발생해야 한다`() {
//        val executor = Executors.newFixedThreadPool(THREAD_COUNT)
//
//        val successCount = mutableListOf<Unit>()
//        val failCount = mutableListOf<Unit>()
//        val lock = Any()
//
//        repeat(THREAD_COUNT) {
//            executor.submit {
//                executeKeywordExtraction(successCount, failCount, lock)
//            }
//        }
//
//        executor.shutdown()
//        val finished = executor.awaitTermination(10, TimeUnit.SECONDS)
//        if (!finished) {
//            throw RuntimeException("Thread pool shutdown timeout occurred")
//        }
//
//        assertEquals(DAILY_LIMIT, successCount.size)
//        assertEquals(THREAD_COUNT - DAILY_LIMIT, failCount.size)
//    }
//
//    private fun executeKeywordExtraction(
//        successCount: MutableList<Unit>,
//        failCount: MutableList<Unit>,
//        lock: Any
//    ) {
//        try {
//            val input = ExtractExperimentPostKeywordsUseCase.Input(memberId, text)
//            useCase.execute(input)
//            synchronized(lock) {
//                successCount.add(Unit)
//            }
//        } catch (e: ExperimentPostKeywordsDailyLimitExceededException) {
//            synchronized(lock) {
//                failCount.add(Unit)
//            }
//        }
//    }
}
