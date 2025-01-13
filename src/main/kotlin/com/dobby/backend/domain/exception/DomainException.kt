package com.dobby.backend.domain.exception

import org.springframework.http.HttpStatus

open class DomainException(
    val errorCode: ErrorCode,
    val data: Any? = null,
) : RuntimeException(errorCode.message)

