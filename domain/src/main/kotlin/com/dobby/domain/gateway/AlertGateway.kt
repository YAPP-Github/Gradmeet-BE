package com.dobby.domain.gateway

interface AlertGateway {
    fun sendError(e: Exception, requestUrl: String, clientIp: String?)
}
