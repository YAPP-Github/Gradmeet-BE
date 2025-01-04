package com.dobby.backend.infrastructure.feign

import com.dobby.backend.presentation.api.dto.request.GoogleTokenRequest
import com.dobby.backend.presentation.api.dto.request.NaverTokenRequest
import com.dobby.backend.presentation.api.dto.response.auth.NaverTokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "naver-auth-feign-client",
    url = "https://nid.naver.com/oauth2.0/token"
)
interface NaverAuthFeignClient {
    @PostMapping
    fun getAccessToken(
        @RequestBody naverTokenRequest: NaverTokenRequest
    ): NaverTokenResponse
}
