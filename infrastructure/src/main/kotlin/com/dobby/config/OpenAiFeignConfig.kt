package com.dobby.config

import com.dobby.config.properties.OpenAiProperties
import feign.RequestInterceptor
import org.springframework.context.annotation.Bean

class OpenAiFeignConfig(
    private val openAiProperties: OpenAiProperties
) {
    @Bean
    fun openAiRequestInterceptor(): RequestInterceptor {
        return RequestInterceptor { template ->
            template.header("Authorization", "Bearer ${openAiProperties.api.key}")
            template.header("Content-Type", "application/json")
        }
    }
}
