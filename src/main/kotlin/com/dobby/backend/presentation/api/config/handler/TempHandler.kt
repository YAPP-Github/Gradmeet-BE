package com.dobby.backend.presentation.api.config.handler

import com.dobby.backend.presentation.api.dto.apiPayload.code.BaseErrorCode
import com.dobby.backend.presentation.api.config.exception.GeneralExcpetion

class TempHandler(errorCode: BaseErrorCode) : GeneralExcpetion(errorCode)