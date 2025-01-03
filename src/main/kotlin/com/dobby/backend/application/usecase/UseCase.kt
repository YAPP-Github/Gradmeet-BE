package com.dobby.backend.application.usecase

fun interface UseCase<I, O> {
    fun execute(input: I): O
}
