package com.dobby.backend.domain.gateway.email

interface EmailGateway {
    suspend fun sendEmail(to: String, subject: String, content: String)
}
