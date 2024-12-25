package com.dobby.backend.presentation.api.dto.apiPayload.code

interface BaseErrorCode {
    fun getReason(): ReasonDto
    fun getReasonHttpStatus(): ReasonDto
}