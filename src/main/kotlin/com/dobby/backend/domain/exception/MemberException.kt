package com.dobby.backend.domain.exception

open class MemberException(
    errorCode: ErrorCode,
) : DomainException(errorCode)

class MemberNotFoundException : MemberException(ErrorCode.MEMBER_NOT_FOUND)
class AlreadyMemberException: MemberException(ErrorCode.SIGNUP_ALREADY_MEMBER)
class SignInMemberException: MemberException(ErrorCode.SIGNIN_MEMBER_NOT_FOUND)
class RoleUnsupportedException: MemberException(ErrorCode.SIGNUP_UNSUPPORTED_ROLE)
class EmailNotValidateException: MemberException(ErrorCode.SIGNUP_EMAIL_NOT_VALIDATED)
