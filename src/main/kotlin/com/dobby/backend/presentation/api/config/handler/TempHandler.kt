package com.dobby.backend.domain.exception.handler

import com.dobby.backend.domain.apiPayload.code.BaseErrorCode
import com.dobby.backend.domain.exception.GeneralExcpetion

class TempHandler(errorCode: BaseErrorCode) : GeneralExcpetion(errorCode)