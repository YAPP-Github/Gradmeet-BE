package com.dobby.backend.domain.exception

import org.springframework.http.HttpStatus

open class DomainException(
    private val errorCode: DomainErrorCode
) : RuntimeException(errorCode.message) {
    val code: String get() = errorCode.code
    val errorMessage: String get() = errorCode.message
    val httpStatus: HttpStatus get()= errorCode.httpStatus
}
