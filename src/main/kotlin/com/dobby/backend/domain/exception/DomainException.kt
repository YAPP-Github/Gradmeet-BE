package com.dobby.backend.domain.exception

open class DomainException(
    val errorCode: ErrorCode,
    val data: Any? = null,
) : RuntimeException(errorCode.message)
