package com.dobby.backend.domain.exception

open class MemberException(
    errorCode: ErrorCode,
) : DomainException(errorCode)

class MemberNotFoundException : MemberException(ErrorCode.MEMBER_NOT_FOUND)
class AlreadyMemberException: MemberException(ErrorCode.SIGNUP_ALREADY_MEMBER)
class LoginMemberException: MemberException(ErrorCode.LOGIN_MEMBER_NOT_FOUND)