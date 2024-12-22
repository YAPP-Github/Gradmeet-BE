package com.dobby.backend.domain.exception

import com.dobby.backend.presentation.api.dto.apiPayload.code.BaseErrorCode
import com.dobby.backend.presentation.api.dto.apiPayload.code.ErrorReasonDto

open class GeneralExcpetion(
    private val code: BaseErrorCode
) : RuntimeException() {

    fun getErrorReason(): ErrorReasonDto {
        return this.code.getReason()
    }
    fun getErrorReasonHttpStatus(): ErrorReasonDto {
        return this.code.getReasonHttpStatus()
    }

}