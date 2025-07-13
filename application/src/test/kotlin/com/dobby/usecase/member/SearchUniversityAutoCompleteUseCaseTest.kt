package com.dobby.usecase.member

import com.dobby.gateway.CacheGateway
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class SearchUniversityAutoCompleteUseCaseTest : BehaviorSpec({
    val cacheGateway = mockk<CacheGateway>(relaxed = true)
    val useCase = SearchUniversityAutoCompleteUseCase(cacheGateway)

    given("대학 자동완성 검색 유즈케이스가 주어졌을 때") {

        `when`("정확한 이름 일부를 입력하면") {
            val input = SearchUniversityAutoCompleteUseCase.Input("서울")
            every { cacheGateway.getAutoComplete(any()) } returns null

            then("해당 키워드를 포함하는 대학교 목록이 반환된다") {
                val result = useCase.execute(input)
                result.output.shouldNotBeEmpty()
                result.output.any { it.contains("서울") } shouldBe true
            }
        }

        `when`("초성으로 입력하면") {
            val input = SearchUniversityAutoCompleteUseCase.Input("ㅇㅎㅇㅈㄷㅎㄱ")
            every { cacheGateway.getAutoComplete(any()) } returns null

            then("이화여자대학교가 포함된 결과가 반환된다") {
                val result = useCase.execute(input)
                result.output.shouldNotBeEmpty()
                result.output.any { it == "이화여자대학교" } shouldBe true
            }
        }

        `when`("존재하지 않는 키워드를 입력하면") {
            val input = SearchUniversityAutoCompleteUseCase.Input("무무대학교")
            every { cacheGateway.getAutoComplete(any()) } returns null

            then("빈 리스트가 반환된다") {
                val result = useCase.execute(input)
                result.output.shouldBeEmpty()
            }
        }
    }

    given("캐시에 값이 있는 경우") {
        val query = "서울대학교"
        val cleanedQuery = "서울"
        val cached = listOf("서울대학교")

        every { cacheGateway.getAutoComplete("autocomplete:$cleanedQuery") } returns cached

        `when`("같은 키워드로 실행하면") {
            val result = useCase.execute(SearchUniversityAutoCompleteUseCase.Input(query))

            then("캐시된 결과가 반환된다") {
                result.output shouldBe cached
            }
        }
    }
})
