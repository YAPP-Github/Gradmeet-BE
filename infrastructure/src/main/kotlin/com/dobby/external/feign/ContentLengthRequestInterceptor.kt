package com.dobby.external.feign

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.stereotype.Component

@Component
class ContentLengthRequestInterceptor : RequestInterceptor {
    companion object {
        private const val CONTENT_LENGTH_HEADER = "Content-Length"
        private const val GOOGLE_OAUTH_HOST = "oauth2.googleapis.com"
    }

    override fun apply(requestTemplate: RequestTemplate) {
        val targetUrl = requestTemplate.feignTarget().url()
        if (targetUrl.contains(GOOGLE_OAUTH_HOST) && requestTemplate.method() == "POST") {
            requestTemplate.body("")
            requestTemplate.header(CONTENT_LENGTH_HEADER, "0")
        }
    }
}
