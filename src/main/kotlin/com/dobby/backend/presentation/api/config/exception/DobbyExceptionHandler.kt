package com.dobby.backend.presentation.api.config.exception

import com.dobby.exception.DobbyException
import com.dobby.backend.presentation.api.dto.response.ExceptionResponse
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
