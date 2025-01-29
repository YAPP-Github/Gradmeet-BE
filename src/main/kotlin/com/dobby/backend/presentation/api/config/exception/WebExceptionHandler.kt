package com.dobby.backend.presentation.api.config.exception

import com.dobby.backend.domain.exception.*
import com.dobby.backend.domain.gateway.AlertGateway
import com.dobby.backend.presentation.api.dto.response.ExceptionResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException

typealias ExceptionResponseEntity = ResponseEntity<ExceptionResponse>

@RestControllerAdvice
class WebExceptionHandler(
    private val alertGateway: AlertGateway,
    private val environment: Environment,
    private val responseFactory: ExceptionResponseFactory,
) {
    private val log: Logger = LoggerFactory.getLogger(WebExceptionHandler::class.java)

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    protected fun handleMethodNotAllowedException(exception: HttpRequestMethodNotSupportedException): ExceptionResponseEntity {
        log.warn("Handling MethodNotAllowedException: ${exception.message}")
        return responseFactory.create(HttpStatus.METHOD_NOT_ALLOWED, InvalidRequestValueException)
    }

    @ExceptionHandler(
        value = [
            MethodArgumentNotValidException::class,
            MethodArgumentTypeMismatchException::class,
            ConstraintViolationException::class,
            BindException::class
        ]
    )
    protected fun handleBadRequestException(exception: Exception): ExceptionResponseEntity {
        log.warn("Handling ${exception::class.simpleName}: ${exception.message}")
        return responseFactory.create(InvalidRequestValueException)
    }

    @ExceptionHandler(NoResourceFoundException::class)
    protected fun handleNotFoundException(exception: NoResourceFoundException): ExceptionResponseEntity {
        log.warn("Handling ${exception::class.simpleName}: ${exception.message}")
        return responseFactory.create(HttpStatus.NOT_FOUND, InvalidRequestValueException)
    }

    @ExceptionHandler(DobbyException::class)
    protected fun handleDobbyException(exception: DobbyException): ExceptionResponseEntity {
        log.warn("Handling DobbyException: ${exception.code} - ${exception.message}")
        return responseFactory.create(exception)
    }

    @ExceptionHandler(
        value = [
            ServerException::class,
            Throwable::class
        ]
    )
    fun handleUnhandledException(
        exception: Exception,
        request: HttpServletRequest
    ): ExceptionResponseEntity {
        if (isProductionOrDevelopmentInstance()) {
            alertGateway.sendError(exception, request)
        }

        log.error("[UnhandledException] " + exception.stackTraceToString())
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ExceptionResponse(UnknownServerErrorException.code, UnknownServerErrorException.message ?: "Internal server error"))
    }

    /**
     * 현재 인스턴스가 프로덕션 또는 개발 환경인지 확인
     */
    private fun isProductionOrDevelopmentInstance(): Boolean {
        return environment.activeProfiles.contains("prod") || environment.activeProfiles.contains("dev")
    }
}
