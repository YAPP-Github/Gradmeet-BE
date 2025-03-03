package com.dobby.external.gateway

import com.dobby.config.properties.UrlProperties
import com.dobby.gateway.UrlGeneratorGateway
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
