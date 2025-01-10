package com.dobby.backend.domain.exception

open class ResearcherException  (
    errorCode: ErrorCode,
) : DomainException(errorCode)
class ResearcherNotFoundException : ResearcherException(ErrorCode.RESEARCHER_NOT_FOUND)
