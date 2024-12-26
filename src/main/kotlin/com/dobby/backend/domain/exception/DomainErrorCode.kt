package com.dobby.backend.domain.exception

import org.springframework.http.HttpStatus

enum class DomainErrorCode(
    val code: String,
    val message: String,
    val httpStatus: HttpStatus
) {
    //MEMBER_DUPLICATE("MEMBER_DUPLICATE", "The member already exists.", HttpStatus.BAD_REQUEST)
}
