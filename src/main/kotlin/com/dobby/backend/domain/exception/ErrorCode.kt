package com.dobby.backend.domain.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val code: String,
    val message: String,
    val httpStatus: HttpStatus
) {
    /**
     * Common error codes
     */
    UNKNOWN_SERVER_ERROR("DB0001", "An unknown error has occurred", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED("DB0002", "Unauthorized access", HttpStatus.UNAUTHORIZED),
    INVALID_INPUT("DB0003", "Missing or invalid input (body or parameters)", HttpStatus.BAD_REQUEST),
    UNKNOWN_RESOURCE("DB0004", "Resource not found", HttpStatus.NOT_FOUND),
    INVALID_METHOD("DB0005", "Invalid request method. Please check the API documentation", HttpStatus.METHOD_NOT_ALLOWED),

    /**
     * Authentication error codes
     */
    TOKEN_NOT_PROVIDED("AU0001", "Authentication token is missing.", HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_VALID("AU0002", "Authentication token is not valid.", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("AU0003", "Authentication token has expired.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN_TYPE("AU0004", "Invalid token type", HttpStatus.UNAUTHORIZED),

    /**
     * Authorization error codes
     */
    PERMISSION_DENIED("AZ0001", "Permission denied", HttpStatus.FORBIDDEN),

    /**
     * Member error codes
     */
    MEMBER_NOT_FOUND("ME0001", "Member not found", HttpStatus.NOT_FOUND),
}
