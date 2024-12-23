package com.dobby.backend.presentation.api.dto.apiPayload.code

data class ReasonDto(
    val code: String,
    val message: String,
    val isSuccess: Boolean
)