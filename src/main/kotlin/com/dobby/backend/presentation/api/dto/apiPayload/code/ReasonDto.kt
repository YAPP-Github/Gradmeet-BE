package com.dobby.backend.presentation.api.dto.apiPayload.code

import org.springframework.http.HttpStatus

data class ReasonDto(
    val code: String,
    val message: String,
    val isSuccess: Boolean,
    val httpStatus: HttpStatus? = null
)