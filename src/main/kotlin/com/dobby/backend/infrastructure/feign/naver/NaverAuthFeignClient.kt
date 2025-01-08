package com.dobby.backend.infrastructure.feign.naver

import com.dobby.backend.presentation.api.dto.request.auth.NaverTokenRequest
import com.dobby.backend.presentation.api.dto.response.auth.naver.NaverTokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

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
