package com.dobby.backend.infrastructure.gateway

import com.dobby.gateway.UrlGeneratorGateway
import com.dobby.backend.infrastructure.config.properties.UrlProperties
import org.springframework.stereotype.Component

@Component
class UrlGeneratorGatewayImpl(
    private val urlProperties: UrlProperties
) : UrlGeneratorGateway {
    override fun getBaseUrl(): String {
        return urlProperties.baseUrl
    }

    override fun getExperimentPostUrl(postId: String): String {
        return urlProperties.baseUrl+"/post/"+postId
    }
}
