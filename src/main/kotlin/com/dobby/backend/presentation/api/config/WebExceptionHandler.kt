package com.dobby.backend.presentation.api.config

import com.dobby.backend.domain.exception.AuthenticationException
import com.dobby.backend.domain.exception.AuthorizationException
import com.dobby.backend.domain.exception.DomainException
import com.dobby.backend.domain.exception.PermissionDeniedException
import com.dobby.backend.presentation.api.dto.payload.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class WebExceptionHandler {

    @ExceptionHandler(value = [DomainException::class])
    fun handleDomainException(
        e: DomainException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity
            .badRequest()
            .body(e.toErrorResponse())
    }

    @ExceptionHandler(value = [AuthenticationException::class])
    fun handleAuthenticationException(
        exception: AuthenticationException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(exception.toErrorResponse())
    }

    @ExceptionHandler(value = [AuthorizationException::class])
    fun handleAuthorizationException(
        exception: AuthorizationException,
    ): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(exception.toErrorResponse())
    }

    @ExceptionHandler(value = [AccessDeniedException::class])
    fun handleAccessDeniedException() = handleAuthorizationException(PermissionDeniedException())

    private fun DomainException.toErrorResponse(): ApiResponse<Unit> {
        return ApiResponse.onFailure(
            code = this.code,
            message = this.errorMessage
        )
    }
}
