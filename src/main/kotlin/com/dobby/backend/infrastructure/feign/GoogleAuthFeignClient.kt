package com.dobby.backend.infrastructure.feign

import com.dobby.backend.presentation.api.dto.request.GoogleTokenRequest
import com.dobby.backend.presentation.api.dto.response.GoogleTokenResponse
import feign.Headers
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "google-auth-feign-client",
    url = "https://oauth2.googleapis.com",
)
interface GoogleAuthFeignClient {
    @PostMapping("/token")
    fun getAccessToken(@RequestBody googleTokenRequest: GoogleTokenRequest
    ): GoogleTokenResponse
}