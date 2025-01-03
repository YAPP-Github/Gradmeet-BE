package com.dobby.backend.domain.exception

import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import org.springframework.http.HttpStatus

class SignInRoleMismatchException(
    registeredRole: RoleType?,
    requestedRole: RoleType
) : RuntimeException(
    String.format(
        ErrorCode.SIGNIN_ROLE_MISMATCH.message,
        registeredRole?.name,
        requestedRole.name
    )
) {
    val code: String = ErrorCode.SIGNIN_ROLE_MISMATCH.code
    val status: HttpStatus = ErrorCode.SIGNIN_ROLE_MISMATCH.httpStatus
}
