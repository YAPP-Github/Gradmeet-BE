package com.dobby.backend.domain.apiPayload.code

data class ReasonDto(
    val code: String,
    val message: String,
    val isSuccess: Boolean
)