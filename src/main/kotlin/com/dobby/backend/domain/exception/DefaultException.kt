package com.dobby.backend.domain.exception

open class DefaultException(
    errorCode: ErrorCode,
) : DomainException(errorCode)

class UnknownErrorException : DefaultException(ErrorCode.UNKNOWN_SERVER_ERROR)
class UnauthorizedException : DefaultException(ErrorCode.UNAUTHORIZED)
class InvalidInputException : DefaultException(ErrorCode.INVALID_INPUT)
class UnknownResourceException : DefaultException(ErrorCode.UNKNOWN_RESOURCE)
