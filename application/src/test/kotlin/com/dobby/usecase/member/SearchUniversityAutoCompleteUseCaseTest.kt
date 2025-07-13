package com.dobby.usecase.member

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe

class SearchUniversityAutoCompleteUseCaseTest : BehaviorSpec({
    val useCase = SearchUniversityAutoCompleteUseCase()

    given("대학 자동완성 검색 유즈케이스가 주어졌을 때") {

        `when`("정확한 이름 일부를 입력하면") {
            val input = SearchUniversityAutoCompleteUseCase.Input("서울")

            then("해당 키워드를 포함하는 대학교 목록이 반환된다") {
                val result = useCase.execute(input)
                result.output.shouldNotBeEmpty()
                result.output.any { it.contains("서울") } shouldBe true
            }
        }

        `when`("초성으로 입력하면") {
            val input = SearchUniversityAutoCompleteUseCase.Input("ㅇㅎㅇㅈㄷㅎㄱ")

            then("이화여자대학교가 포함된 결과가 반환된다") {
                val result = useCase.execute(input)
                result.output.shouldNotBeEmpty()
                result.output.any { it == "이화여자대학교" } shouldBe true
            }
        }

        `when`("존재하지 않는 키워드를 입력하면") {
            val input = SearchUniversityAutoCompleteUseCase.Input("무무대학교")

            then("빈 리스트가 반환된다") {
                val result = useCase.execute(input)
                result.output.shouldBeEmpty()
            }
        }
    }
})
