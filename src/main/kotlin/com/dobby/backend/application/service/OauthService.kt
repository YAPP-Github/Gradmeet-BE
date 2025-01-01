package com.dobby.backend.application.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class OauthService(
    private val webClientBuilder: WebClient.Builder,
) {
    fun getGoogleUserInfo(accessToken: String): Map<String, Any> {
        val webClient = webClientBuilder.build()
        val response = webClient.get()
            .uri("https://www.googleapis.com/oauth2/v3/userinfo")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .bodyToMono(Map::class.java)
            .block()

        return response as Map<String, Any>
    }
}