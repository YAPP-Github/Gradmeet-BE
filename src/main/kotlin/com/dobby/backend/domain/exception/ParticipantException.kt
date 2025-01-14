package com.dobby.backend.domain.exception

open class ParticipantException  (
    errorCode: ErrorCode,
) : DomainException(errorCode)
class ParticipantNotFoundException : ParticipantException(ErrorCode.PARTICIPANT_NOT_FOUND)
