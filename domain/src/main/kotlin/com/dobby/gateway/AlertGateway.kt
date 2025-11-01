package com.dobby.gateway

interface AlertGateway {
    fun sendError(e: Exception, requestUrl: String, clientIp: String?)
    fun sendNotify(title: String, description: String, content: String?)
}
