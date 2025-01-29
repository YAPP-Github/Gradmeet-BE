package com.dobby.backend.domain.exception

class SignInRoleMismatchException(
    existingRole: String,
) : DomainException(
    errorCode = ErrorCode.SIGNIN_ROLE_MISMATCH,
    data = mapOf(
        "existingRole" to existingRole
    )
)
