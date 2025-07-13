package com.dobby.usecase.member

import com.dobby.enums.University
import com.dobby.gateway.CacheGateway
import com.dobby.usecase.UseCase

class SearchUniversityAutoCompleteUseCase(
    private val cacheGateway: CacheGateway
) : UseCase<SearchUniversityAutoCompleteUseCase.Input, SearchUniversityAutoCompleteUseCase.Output> {

    data class Input(
        val query: String
    )

    data class Output(
        val output: List<String>
    )

    override fun execute(input: Input): Output {
        val cleanedQuery = input.query.clean()
        val cacheKey = "autocomplete:$cleanedQuery"

        cacheGateway.getAutoComplete(cacheKey)?.let {
            return Output(it)
        }

        val result = University.match(cleanedQuery)

        cacheGateway.setAutoComplete(cacheKey, result)
        return Output(
            output = result
        )
    }

    private fun String.clean(): String {
        val suffixes = listOf("대학교", "캠퍼스")

        for (suffix in suffixes) {
            if (this.endsWith(suffix)) {
                return this.removeSuffix(suffix)
            }
        }
        return this
    }
}
