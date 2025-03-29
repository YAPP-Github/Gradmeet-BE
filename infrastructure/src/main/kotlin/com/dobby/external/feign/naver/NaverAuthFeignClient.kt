package com.dobby.external.feign.naver

import com.dobby.api.dto.response.auth.naver.NaverTokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "naver-auth-feign-client",
    url = "https://nid.naver.com/oauth2.0/token"
)
interface NaverAuthFeignClient {
    @PostMapping
    fun getAccessToken(
        @RequestParam(name = "code") code: String,
        @RequestParam(name = "client_id") clientId: String,
        @RequestParam(name = "client_secret") clientSecret: String,
        @RequestParam(name = "state") state: String,
        @RequestParam(name = "grant_type") grantType: String
    ): NaverTokenResponse
}
