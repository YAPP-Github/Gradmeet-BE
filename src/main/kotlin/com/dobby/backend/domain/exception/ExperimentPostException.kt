package com.dobby.backend.domain.exception

open class ExperimentPostException(
    errorCode: ErrorCode,
) : DomainException(errorCode)

class ExperimentPostNotFoundException : ExperimentPostException(ErrorCode.EXPERIMENT_POST_NOT_FOUND)
