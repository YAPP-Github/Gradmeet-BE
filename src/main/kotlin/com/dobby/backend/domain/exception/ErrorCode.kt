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
    INVALID_TOKEN_VALUE("AU0005", "Invalid token value", HttpStatus.UNAUTHORIZED),

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
    SIGNUP_ALREADY_MEMBER("SIGN_UP_001", "You've already joined", HttpStatus.CONFLICT),
    SIGNUP_UNSUPPORTED_ROLE("SIGN_UP_002", "Requested RoleType does not supported", HttpStatus.BAD_REQUEST),
    SIGNUP_EMAIL_NOT_VALIDATED("SIGN_UP_003", "You should validate your school email first", HttpStatus.BAD_REQUEST),

    /**
     * Signin error codes
     */
    SIGNIN_MEMBER_NOT_FOUND("SIGN_IN_001", "Member Not Found. Please signup. (Redirect URI must be /v1/auth/{role}/signup)", HttpStatus.NOT_FOUND),
    SIGNIN_ROLE_MISMATCH("SIGN_IN_002", "Already registered member as %s. Please login as %s", HttpStatus.BAD_REQUEST),

    /**
     * Email Verification error codes
     */
    VERIFY_EMAIL_NOT_FOUND("VE001", "Email domain not found as real email domain", HttpStatus.BAD_REQUEST),
    VERIFY_UNIV_NOT_FOUND("VER002", "Email domain not found as university email", HttpStatus.BAD_REQUEST),
    VERIFY_INFO_NOT_FOUND("VER003", "Verification information is not found", HttpStatus.NOT_FOUND),
    VERIFY_CODE_NOT_CORRECT("VER004", "Verification code is not correct", HttpStatus.BAD_REQUEST),
    VERIFY_CODE_EXPIRED("VER005", "Verification code is expired", HttpStatus.BAD_REQUEST),
}
