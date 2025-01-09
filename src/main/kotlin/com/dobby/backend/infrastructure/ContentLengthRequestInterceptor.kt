package com.dobby.backend.infrastructure

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.stereotype.Component


@Component
class ContentLengthRequestInterceptor : RequestInterceptor {
    override fun apply(requestTemplate: RequestTemplate) {
        val length = if (requestTemplate.body() != null) requestTemplate.body().size else 0
        if (length == 0 && requestTemplate.method() == "POST") {
            requestTemplate.body("gradmeet")
            requestTemplate.header(CONTENT_LENGTH_HEADER, "gradmeet".toByteArray().size.toString())
        }
    }

    companion object {
        private const val CONTENT_LENGTH_HEADER = "Content-Length"
    }
}
