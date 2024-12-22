package com.dobby.backend.domain.apiPayload.code

interface BaseErrorCode {
    fun getReason(): ErrorReasonDto
    fun getReasonHttpStatus(): ErrorReasonDto
}