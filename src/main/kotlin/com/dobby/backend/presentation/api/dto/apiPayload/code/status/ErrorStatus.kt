package com.dobby.backend.presentation.api.dto.apiPayload.code.status

import com.dobby.backend.presentation.api.dto.apiPayload.code.BaseErrorCode
import com.dobby.backend.presentation.api.dto.apiPayload.code.ReasonDto
import org.springframework.http.HttpStatus

enum class ErrorStatus(
    private val httpStatus: HttpStatus,
    private val code: String,
    private val message: String
): BaseErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),;

    override fun getReason(): ReasonDto {
        return ReasonDto(
            message = message,
            code = code,
            isSuccess = false
        )
    }
    override fun getReasonHttpStatus(): ReasonDto {
        return ReasonDto(
            message = message,
            code = code,
            isSuccess = false,
            httpStatus = httpStatus
        )
    }
}