package com.dobby.backend.infrastructure.feign.google

import com.dobby.backend.presentation.api.dto.response.auth.GoogleInfoResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    name= "google-userinfo-feign-client",
    url = "https://www.googleapis.com"
)
interface GoogleUserInfoFeginClient {
    @GetMapping("/oauth2/v3/userinfo?access_token={OAUTH_TOKEN}")
    fun getUserInfo(@PathVariable("OAUTH_TOKEN")token: String): GoogleInfoResponse
}
