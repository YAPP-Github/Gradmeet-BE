package com.dobby.api.config.exception

import com.dobby.api.dto.response.ExceptionResponse
import com.dobby.exception.DobbyException
import org.springframework.stereotype.Component

@Component
class DobbyExceptionHandler {
    fun convert(exception: DobbyException): ExceptionResponse {
        return ExceptionResponse(
            code = exception.code,
            message = exception.message ?: "Unexpected error occurred"
        )
    }
}
