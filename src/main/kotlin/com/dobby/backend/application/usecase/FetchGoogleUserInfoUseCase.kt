package com.dobby.backend.application.usecase

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class FetchGoogleUserInfoUseCase(
    private val webClientBuilder : WebClient.Builder
){
    fun execute(accessToken: String) : Map<String, Any>{
        val webClient = webClientBuilder.build()
        return webClient.get()
            .uri("https://www.googleapis.com/oauth2/v3/userinfo")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .bodyToMono(Map::class.java)
            .block() as? Map<String, Any> ?: throw IllegalArgumentException("Google 사용자 정보를 가져오지 못했습니다.")
    }
}