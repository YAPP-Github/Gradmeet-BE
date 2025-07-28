package com.dobby.external.feign

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.stereotype.Component

@Component
class ContentLengthRequestInterceptor : RequestInterceptor {
    override fun apply(requestTemplate: RequestTemplate) {
        if (requestTemplate.feignTarget().url().contains("oauth2.googleapis.com")) {
            requestTemplate.body("")
            requestTemplate.header(CONTENT_LENGTH_HEADER, "0")
        }
    }

    companion object {
        private const val CONTENT_LENGTH_HEADER = "Content-Length"
    }
}
