package com.dobby.backend.application.usecase

fun interface AsyncUseCase<I, O>{
    suspend fun execute(input: I): O
}
