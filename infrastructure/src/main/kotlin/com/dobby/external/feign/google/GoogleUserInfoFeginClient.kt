package com.dobby.external.feign.google

import com.dobby.api.dto.response.auth.google.GoogleInfoResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "google-userinfo-feign-client",
    url = "https://www.googleapis.com"
)
interface GoogleUserInfoFeginClient {
    @GetMapping("/oauth2/v3/userinfo")
    fun getUserInfo(
        @RequestHeader("Authorization") token: String
    ): GoogleInfoResponse
}
