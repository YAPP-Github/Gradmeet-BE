package com.dobby.backend.domain.exception

import org.springframework.http.HttpStatus

/**
 * Top-level exception class for Dobby application
 */
sealed class DobbyException(val code: String, message: String, val httpStatus: HttpStatus, cause: Throwable? = null) : RuntimeException(message, cause)


/**
 * ClientException: Exceptions caused by invalid client requests
 * - Authentication and authorization failures
 * - Invalid or missing request data
 */
sealed class ClientException(code: String, message: String, httpStatus: HttpStatus, cause: Throwable? = null) : DobbyException(code, message, httpStatus, cause)

/**
 * Common error codes
 */
data object UnAuthorizedException : ClientException("DB0002", "Unauthorized access", HttpStatus.UNAUTHORIZED)
data object InvalidRequestValueException : ClientException("DB0003", "Missing or invalid input", HttpStatus.BAD_REQUEST)
data object PermissionDeniedException : ClientException("DB0004", "Permission denied", HttpStatus.FORBIDDEN)

/**
 * Authentication error codes
 */
data object AuthenticationTokenNotFoundException : ClientException("AU0001", "Authentication token is missing.", HttpStatus.UNAUTHORIZED)
data object AuthenticationTokenNotValidException : ClientException("AU0002", "Authentication token is not valid.", HttpStatus.UNAUTHORIZED)
data object AuthenticationTokenExpiredException : ClientException("AU0003", "Authentication token has expired.", HttpStatus.UNAUTHORIZED)
data object InvalidTokenTypeException : ClientException("AU0004", "Invalid token type", HttpStatus.UNAUTHORIZED)
data object InvalidTokenValueException : ClientException("AU0005", "Invalid token value", HttpStatus.UNAUTHORIZED)

/**
 * Email Verification error codes
 */
data object EmailDomainNotFoundException : ClientException("VE0001", "Email domain not found", HttpStatus.BAD_REQUEST)
data object EmailNotUnivException : ClientException("VE0002", "Email domain not found as university email", HttpStatus.BAD_REQUEST)
data object VerifyInfoNotFoundException : ClientException("VE0003", "Verification information is not found", HttpStatus.NOT_FOUND)
data object CodeNotCorrectException : ClientException("VE0004", "Verification code is not correct", HttpStatus.BAD_REQUEST)
data object CodeExpiredException : ClientException("VE0005", "Verification code is expired", HttpStatus.BAD_REQUEST)
data object EmailFormatInvalidException : ClientException("VE0006", "Email is invalid format", HttpStatus.BAD_REQUEST)
data object EmailAlreadyVerifiedException : ClientException("VE0007", "This email is already verified", HttpStatus.CONFLICT)
data object TooManyVerificationRequestException : ClientException("VE0008", "You can request verification up to 3 in one day.", HttpStatus.TOO_MANY_REQUESTS)

/**
 * Member error codes
 */
data object MemberNotFoundException : ClientException("ME0001", "Member not found", HttpStatus.NOT_FOUND)
data class MemberRoleMismatchException(val role: String) : ClientException("ME0002", "Already registered as another role: $role", HttpStatus.BAD_REQUEST)
data object ResearcherNotFoundException : ClientException("ME0003", "Researcher Not Found.", HttpStatus.NOT_FOUND)
data object ParticipantNotFoundException : ClientException("ME0004", "Participant Not Found.", HttpStatus.NOT_FOUND)
data object EmailNotValidateException : ClientException("ME0005", "You should validate your school email first", HttpStatus.BAD_REQUEST)
data object SignupOauthEmailDuplicateException : ClientException("ME0006", "You've already joined with requested oauth email", HttpStatus.CONFLICT)
data object ContactEmailDuplicateException: ClientException("ME0007", "This contact email is already in use.", HttpStatus.CONFLICT)
data object MemberConsentNotFoundException : ClientException("ME0008", "Member Consent Not Found.", HttpStatus.NOT_FOUND)

/**
 * Experiment error codes
 */
data object ExperimentPostNotFoundException : ClientException("EP0001", "Experiment Post Not Found.", HttpStatus.NOT_FOUND)
data object ExperimentAreaOverflowException : ClientException("EP0003", "You can only select up to 5 Area options in 1 Region.", HttpStatus.BAD_REQUEST)
data object ExperimentAreaInCorrectException : ClientException("EP0004", "Selected Area doesn't belong to correct Region.", HttpStatus.BAD_REQUEST)
data object ExperimentPostImageSizeException : ClientException("EP0005", "Image can be uploaded maximum 3 images.", HttpStatus.BAD_REQUEST)
data object ExperimentPostRecruitStatusException : ClientException("EP0006", "You cannot update recruitStatus with closed recruitment post.", HttpStatus.BAD_REQUEST)
data object ExperimentPostUpdateDateException : ClientException("EP0007", "You cannot update startDate, endDate with closed recruitment post.", HttpStatus.BAD_REQUEST)
data object ExperimentPostInvalidOnlineRequestException : ClientException("EP0008", "place, region, area field value must be null when MatchType is online.", HttpStatus.BAD_REQUEST)
data object ExperimentPostTitleException : ClientException ("EP0009", "Title cannot be null.", HttpStatus.BAD_REQUEST)
data object ExperimentPostRewardException: ClientException ("EP0010", "Reward cannot be null.", HttpStatus.BAD_REQUEST)
data object ExperimentPostContentException: ClientException ("EP0011", "Content cannot be null.", HttpStatus.BAD_REQUEST)
data object ExperimentPostCountException: ClientException ("EP0012", "Count could be more than zero.", HttpStatus.BAD_REQUEST)
data object ExperimentPostLeadResearcherException: ClientException ("EP0013", "Lead Researcher cannot be null.", HttpStatus.BAD_REQUEST)


/**
 * ServerException: Exceptions caused by internal server issues
 * - Database or infrastructure failures
 * - Unexpected backend processing issues
 */
sealed class ServerException(code: String, message: String, cause: Throwable? = null) : DobbyException(code, message, HttpStatus.INTERNAL_SERVER_ERROR, cause)

data object UnknownServerErrorException : ServerException("DB0001", "An unknown error has occurred")
