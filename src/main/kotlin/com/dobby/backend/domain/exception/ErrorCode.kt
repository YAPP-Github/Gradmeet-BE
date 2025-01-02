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

    /**
     * OAuth2 error codes
     */
    OAUTH_USER_NOT_FOUND("OA001", "Authentication Principal is not an instance of OAuth2User", HttpStatus.BAD_REQUEST),
    OAUTH_PROVIDER_MISSING("OA002", "Cannot Load OAuth2 User Info during the external API calling", HttpStatus.BAD_REQUEST),
    OAUTH_PROVIDER_NOT_FOUND("OA003", "Not supported Provider is requested", HttpStatus.BAD_REQUEST),
    OAUTH_EMAIL_NOT_FOUND("OA004", "Email cannot found in OAuth2 Authentication Info", HttpStatus.BAD_REQUEST),
    OAUTH_NAME_NOT_FOUND("OA005", "Name cannt found in OAuth2 Authentication Info", HttpStatus.BAD_REQUEST),

    /**
     * Signup error codes
     */
    SIGNUP_ALREADY_MEMBER("SI001", "You've already joined", HttpStatus.CONFLICT),

    /**
     * Login error codes
     */
    LOGIN_MEMBER_NOT_FOUND("LO001", "Member Not Found. Please signup. (Redirect URI must be /v1/auth/{role}/signup)", HttpStatus.NOT_FOUND),
    LOGIN_ROLE_MISMATCH("LO002", "Already registered member as %s. Please login as %s", HttpStatus.BAD_REQUEST)
}
