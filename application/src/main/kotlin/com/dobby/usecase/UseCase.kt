package com.dobby.usecase

fun interface UseCase<I, O> {
    fun execute(input: I): O
}
