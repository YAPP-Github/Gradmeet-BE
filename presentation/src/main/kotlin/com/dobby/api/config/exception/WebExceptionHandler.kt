package com.dobby.api.config.exception

import com.dobby.api.dto.response.ExceptionResponse
import com.dobby.exception.DobbyException
import com.dobby.exception.InvalidRequestValueException
import com.dobby.exception.PermissionDeniedException
import com.dobby.exception.ServerException
import com.dobby.exception.UnknownServerErrorException
import com.dobby.gateway.AlertGateway
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authorization.AuthorizationDeniedException
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
    private val responseFactory: ExceptionResponseFactory
) {
    private val log: Logger = LoggerFactory.getLogger(WebExceptionHandler::class.java)

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    protected fun handleMethodNotAllowedException(exception: HttpRequestMethodNotSupportedException): ExceptionResponseEntity {
        log.warn("Handling ${exception::class.simpleName}: ${exception.message}")
        return responseFactory.create(InvalidRequestValueException)
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
        return responseFactory.create(InvalidRequestValueException)
    }

    @ExceptionHandler(
        value = [
            AuthorizationDeniedException::class,
            AccessDeniedException::class
        ]
    )
    protected fun handlePermissionDeniedException(exception: Exception): ExceptionResponseEntity {
        log.warn("Handling ${exception::class.simpleName}: ${exception.message}")
        return responseFactory.create(PermissionDeniedException)
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
            val requestUrl = request.method + " " + request.requestURL.toString()
            val clientIp = request.remoteAddr
            alertGateway.sendError(exception, requestUrl, clientIp)
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
