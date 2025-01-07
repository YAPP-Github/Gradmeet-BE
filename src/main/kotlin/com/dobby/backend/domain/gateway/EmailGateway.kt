package com.dobby.backend.domain.gateway

interface EmailGateway {
    fun sendEmail(to: String, subject: String, content: String)
}
