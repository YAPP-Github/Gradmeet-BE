package com.dobby.backend.domain.exception

open class VerificationException (
    errorCode: ErrorCode,
) : DomainException(errorCode)

class EmailFormatInvalidException : VerificationException(ErrorCode.VERIFY_EMAIL_INVALID_FORMAT)
class EmailDomainNotFoundException : VerificationException(ErrorCode.VERIFY_EMAIL_NOT_FOUND)
class EmailNotUnivException : VerificationException(ErrorCode.VERIFY_UNIV_NOT_FOUND)
class VerifyInfoNotFoundException: VerificationException(ErrorCode.VERIFY_INFO_NOT_FOUND)
class CodeNotCorrectException : VerificationException(ErrorCode.VERIFY_CODE_NOT_CORRECT)
class CodeExpiredException: VerificationException(ErrorCode.VERIFY_CODE_EXPIRED)
class EmailAlreadyVerifiedException: VerificationException(ErrorCode.VERIFY_ALREADY_VERIFIED)
