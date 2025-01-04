package com.dobby.backend.domain.exception

open class AuthenticationException(
    errorCode: ErrorCode,
) : DomainException(errorCode)

class AuthenticationTokenNotFoundException : AuthenticationException(ErrorCode.TOKEN_NOT_PROVIDED)
class AuthenticationTokenNotValidException : AuthenticationException(ErrorCode.TOKEN_NOT_VALID)
class AuthenticationTokenExpiredException : AuthenticationException(ErrorCode.TOKEN_EXPIRED)
class InvalidTokenTypeException : AuthenticationException(ErrorCode.INVALID_TOKEN_TYPE)
class InvalidTokenValueException : AuthenticationException(ErrorCode.INVALID_TOKEN_VALUE)
