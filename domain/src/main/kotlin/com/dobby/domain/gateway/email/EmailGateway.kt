package com.dobby.domain.gateway.email

interface EmailGateway {
    suspend fun sendEmail(to: String, subject: String, content: String, isHtml: Boolean)
}
