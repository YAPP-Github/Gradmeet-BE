package com.dobby.backend.presentation.api.config.exception

import com.dobby.backend.domain.exception.*
import com.dobby.backend.presentation.api.dto.response.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ExceptionResponseFactory(
    private val exceptionHandler: DobbyExceptionHandler,
) {
    /**
     * DobbyException을 받아 ResponseEntity<ExceptionResponse> 형태로 변환
     * 예외 객체에 정의된 HTTP 상태 코드를 사용
     */
    fun create(exception: DobbyException): ResponseEntity<ExceptionResponse> {
        val responseBody = exceptionHandler.convert(exception)
        return ResponseEntity.status(exception.httpStatus).body(responseBody)
    }

    /**
     * HttpStatus를 직접 지정하여 ResponseEntity 생성 가능하도록 오버로딩 추가
     * - `status`가 명시되지 않으면 예외 객체에 설정된 상태 코드 사용
     */
    fun create(status: HttpStatus?, exception: DobbyException): ResponseEntity<ExceptionResponse> {
        val resolvedStatus = status ?: exception.httpStatus
        val responseBody = exceptionHandler.convert(exception)

        return ResponseEntity.status(resolvedStatus).body(responseBody)
    }
}
