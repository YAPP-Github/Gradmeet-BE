package com.dobby.usecase.member

import com.dobby.enums.University
import com.dobby.usecase.UseCase

class SearchUniversityAutoCompleteUseCase(
) : UseCase<SearchUniversityAutoCompleteUseCase.Input, SearchUniversityAutoCompleteUseCase.Output> {

    data class Input(
        val query: String
    )

    data class Output(
        val output: List<University>
    )

    override fun execute(input: Input): Output {
        return Output(
            output = University.match(input.query)
        )
    }
}
