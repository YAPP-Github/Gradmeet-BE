package com.dobby.backend.infrastructure.feign.google

import com.dobby.backend.presentation.api.dto.response.auth.google.GoogleTokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "google-auth-feign-client",
    url = "https://oauth2.googleapis.com/token",
)
interface GoogleAuthFeignClient {
    @PostMapping
    fun getAccessToken(
        @RequestParam(name = "code") code: String,
        @RequestParam(name = "client_id") clientId: String,
        @RequestParam(name = "client_secret") clientSecret: String,
        @RequestParam(name = "redirect_uri") redirectUri: String,
        @RequestParam(name = "grant_type") grantType: String,
    ): GoogleTokenResponse
}
