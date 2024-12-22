package com.dobby.backend.domain.apiPayload.code

import org.springframework.http.HttpStatus

data class ErrorReasonDto(
    val code: String,
    val message: String,
    val success: Boolean = false,
    val httpStatus: HttpStatus? = null
)