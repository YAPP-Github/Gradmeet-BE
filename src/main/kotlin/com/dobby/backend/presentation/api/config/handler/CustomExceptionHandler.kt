package com.dobby.backend.presentation.api.config.handler

import com.dobby.backend.presentation.api.dto.apiPayload.code.BaseErrorCode
import com.dobby.backend.presentation.api.config.exception.GeneralException

class CustomExceptionHandler(errorCode: BaseErrorCode) : GeneralException(errorCode)