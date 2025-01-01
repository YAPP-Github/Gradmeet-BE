package com.dobby.backend.domain.usecase

fun interface UseCase<I, O> {
    fun execute(input: I): O
}
