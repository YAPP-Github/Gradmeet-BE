package com.dobby.backend.presentation.api.config.exception

import com.dobby.backend.presentation.api.dto.payload.code.BaseErrorCode
import com.dobby.backend.presentation.api.dto.payload.code.ReasonDto

open class GeneralException(
    private val code: BaseErrorCode
) : RuntimeException() {

    fun getErrorReason(): ReasonDto {
        return this.code.getReason()
    }
    fun getErrorReasonHttpStatus(): ReasonDto {
        return this.code.getReasonHttpStatus()
    }

}
