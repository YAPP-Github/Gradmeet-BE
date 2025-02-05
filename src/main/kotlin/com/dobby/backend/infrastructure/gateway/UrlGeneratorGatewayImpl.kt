package com.dobby.backend.infrastructure.gateway

import com.dobby.backend.domain.gateway.UrlGeneratorGateway
import com.dobby.backend.infrastructure.config.properties.UrlProperties
import org.springframework.stereotype.Component

@Component
class UrlGeneratorGatewayImpl(
    private val urlProperties: UrlProperties
) : UrlGeneratorGateway{
    override fun getBaseUrl(): String {
        return urlProperties.serverUrl
    }

    override fun getExperimentPostUrl(postId: String): String {
        return urlProperties.serverUrl+"/v1/experiment-posts/"+postId+"/details"
    }
}
