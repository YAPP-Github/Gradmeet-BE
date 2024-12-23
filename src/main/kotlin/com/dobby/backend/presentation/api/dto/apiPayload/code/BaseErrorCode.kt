package com.dobby.backend.presentation.api.dto.apiPayload.code

interface BaseErrorCode {
    fun getReason(): ErrorReasonDto
    fun getReasonHttpStatus(): ErrorReasonDto
}