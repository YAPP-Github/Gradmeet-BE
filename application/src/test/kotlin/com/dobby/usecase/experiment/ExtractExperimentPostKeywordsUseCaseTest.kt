package com.dobby.usecase.experiment

import com.dobby.gateway.OpenAiGateway
import com.dobby.gateway.UsageLimitGateway
import com.dobby.gateway.experiment.ExperimentPostKeywordsLogGateway
import com.dobby.gateway.member.MemberGateway
import com.dobby.model.experiment.ExperimentPostKeywordsLog
import com.dobby.model.experiment.keyword.ExperimentPostKeywords
import com.dobby.model.member.Member
import com.dobby.util.IdGenerator
import com.dobby.util.TimeProvider
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import java.time.LocalDateTime

class ExtractExperimentPostKeywordsUseCaseTest : BehaviorSpec({
    val openAiGateway = mockk<OpenAiGateway>()
    val experimentPostKeywordsLogGateway = mockk<ExperimentPostKeywordsLogGateway>()
    val memberGateway = mockk<MemberGateway>()
    val usageLimitGateway = mockk<UsageLimitGateway>()
    val idGenerator = mockk<IdGenerator>()

    val extractExperimentPostKeywordsUseCase = ExtractExperimentPostKeywordsUseCase(
        openAiGateway,
        experimentPostKeywordsLogGateway,
        memberGateway,
        usageLimitGateway,
        idGenerator
    )

    beforeSpec {
        mockkObject(TimeProvider)
    }

    afterSpec {
        unmockkAll()
    }

    given("일일 사용 한도를 초과하지 않은 사용자가") {
        val memberId = "test_member_123"
        val inputText = "남성 20-30대 대상 설문조사 참여자 모집합니다. 시간은 1시간 소요되며 참가비 10,000원 지급합니다."
        val input = ExtractExperimentPostKeywordsUseCase.Input(memberId, inputText)

        val mockMember = mockk<Member>()
        val mockExperimentPostKeywords = mockk<ExperimentPostKeywords>()
        val mockLog = mockk<ExperimentPostKeywordsLog>()
        val currentDateTime = LocalDateTime.of(2025, 1, 27, 10, 0, 0)

        every { TimeProvider.currentDateTime() } returns currentDateTime
        every { memberGateway.getById(memberId) } returns mockMember
        every { usageLimitGateway.incrementAndCheckLimit(memberId, any()) } returns true
        every { idGenerator.generateId() } returns "test_log_id"
        every { openAiGateway.extractKeywords(inputText) } returns mockExperimentPostKeywords
        every { experimentPostKeywordsLogGateway.save(any()) } returns mockLog

        `when`("키워드 추출을 요청하면") {
            val result = extractExperimentPostKeywordsUseCase.execute(input)

            then("추출된 키워드 정보를 반환해야 한다") {
                result.experimentPostKeywords shouldBe mockExperimentPostKeywords
            }
        }
    }

//    given("일일 사용 한도에 도달한 사용자가") {
//        val memberId = "exceeded_member_456"
//        val inputText = "실험 참여자 모집"
//        val input = ExtractExperimentPostKeywordsUseCase.Input(memberId, inputText)
//
//        val mockMember = mockk<Member>()
//        val currentDateTime = LocalDateTime.of(2025, 1, 27, 15, 30, 0)
//
//        every { TimeProvider.currentDateTime() } returns currentDateTime
//        every { memberGateway.getById(memberId) } returns mockMember
//        every { usageLimitGateway.incrementAndCheckLimit(memberId, any()) } returns false
//
//        `when`("키워드 추출을 요청하면") {
//            then("DailyLimitExceededException 예외가 발생해야 한다") {
//                shouldThrow<ExperimentPostKeywordsDailyLimitExceededException> {
//                    extractExperimentPostKeywordsUseCase.execute(input)
//                }
//            }
//        }
//    }
})
