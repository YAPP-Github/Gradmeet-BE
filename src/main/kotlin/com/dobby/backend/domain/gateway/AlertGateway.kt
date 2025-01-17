package com.dobby.backend.domain.gateway

import jakarta.servlet.http.HttpServletRequest

interface AlertGateway {
    fun sendError(e: Exception, request: HttpServletRequest)
}
