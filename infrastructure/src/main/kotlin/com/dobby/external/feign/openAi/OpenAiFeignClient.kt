package com.dobby.external.feign.openAi

import com.dobby.api.dto.request.OpenAiRequest
import com.dobby.api.dto.response.OpenAiResponse
import com.dobby.config.OpenAiFeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "openAiClient",
    url = "https://api.openai.com/v1",
    configuration = [OpenAiFeignConfig::class]
)
interface OpenAiFeignClient {
    @PostMapping("/chat/completions")
    fun chatCompletion(@RequestBody request: OpenAiRequest): OpenAiResponse
}
