package com.dobby.backend.infrastructure.feign.google

import com.dobby.backend.presentation.api.dto.request.auth.GoogleTokenRequest
import com.dobby.backend.presentation.api.dto.response.auth.GoogleTokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "google-auth-feign-client",
    url = "https://oauth2.googleapis.com",
)
interface GoogleAuthFeignClient {
    @PostMapping("/token")
    fun getAccessToken(@RequestBody googleTokenRequest: GoogleTokenRequest
    ): GoogleTokenResponse
}
