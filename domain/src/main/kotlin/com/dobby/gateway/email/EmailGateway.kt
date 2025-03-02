package com.dobby.gateway.email

interface EmailGateway {
    fun sendEmail(to: String, subject: String, content: String, isHtml: Boolean)
}
