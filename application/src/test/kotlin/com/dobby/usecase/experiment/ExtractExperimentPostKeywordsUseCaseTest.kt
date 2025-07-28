package com.dobby.usecase.experiment

import com.dobby.gateway.experiment.ExperimentKeywordExtractionGateway
import com.dobby.model.experiment.keyword.ExperimentPostKeyword
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class ExtractExperimentPostKeywordsUseCaseTest : BehaviorSpec({
    val experimentKeywordExtractionGateway = mockk<ExperimentKeywordExtractionGateway>()
    val extractExperimentPostKeywordsUseCase = ExtractExperimentPostKeywordsUseCase(experimentKeywordExtractionGateway)

    given("실험 게시글 텍스트에서 키워드를 추출할 때") {
        val inputText = "남성 20-30대 대상 설문조사 참여자 모집합니다. 시간은 1시간 소요되며 참가비 10,000원 지급합니다."
        val input = ExtractExperimentPostKeywordsUseCase.Input(inputText)
        val mockExperimentPostKeyword = mockk<ExperimentPostKeyword>()

        every { experimentKeywordExtractionGateway.extractKeywords(inputText) } returns mockExperimentPostKeyword

        `when`("키워드 추출을 요청하면") {
            val result = extractExperimentPostKeywordsUseCase.execute(input)

            then("추출된 키워드 정보를 반환해야 한다") {
                result.experimentPostKeyword shouldBe mockExperimentPostKeyword

                verify(exactly = 1) {
                    experimentKeywordExtractionGateway.extractKeywords(inputText)
                }
            }
        }
    }
})
