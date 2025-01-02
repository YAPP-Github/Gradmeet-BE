package com.dobby.backend.application.usecase

import com.dobby.backend.domain.exception.OAuth2ProviderMissingException
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class FetchGoogleUserInfoUseCase(
    private val webClientBuilder : WebClient.Builder
){
    fun execute(accessToken: String) : Map<String, Any>{
        try {
            val webClient = webClientBuilder.build()
            return webClient.get()
                .uri("https://www.googleapis.com/oauth2/v3/userinfo")
                .header("Authorization", "Bearer $accessToken")
                .retrieve()
                .bodyToMono<Map<String, Any>>()
                .block() ?: throw OAuth2ProviderMissingException()
        } catch (e : WebClientResponseException) {
            throw OAuth2ProviderMissingException()
        }
    }
}