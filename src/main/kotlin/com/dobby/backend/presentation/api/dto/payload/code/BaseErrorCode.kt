package com.dobby.backend.presentation.api.dto.payload.code

interface BaseErrorCode {
    fun getReason(): ReasonDto
    fun getReasonHttpStatus(): ReasonDto
}