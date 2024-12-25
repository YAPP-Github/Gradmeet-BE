package com.dobby.backend.presentation.api.config.exception

import com.dobby.backend.presentation.api.dto.payload.code.BaseErrorCode
import com.dobby.backend.presentation.api.dto.payload.code.ErrorReasonDto

open class GeneralException(
    private val code: BaseErrorCode
) : RuntimeException() {

    fun getErrorReason(): ErrorReasonDto {
        return this.code.getReason()
    }
    fun getErrorReasonHttpStatus(): ErrorReasonDto {
        return this.code.getReasonHttpStatus()
    }

}