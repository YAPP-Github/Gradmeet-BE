package com.dobby.domain.gateway.email

interface EmailGateway {
    fun sendEmail(to: String, subject: String, content: String, isHtml: Boolean)
}
