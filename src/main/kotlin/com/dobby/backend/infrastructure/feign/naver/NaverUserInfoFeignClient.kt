package com.dobby.backend.infrastructure.feign.naver

import com.dobby.backend.presentation.api.dto.response.auth.naver.NaverInfoResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "naver-userinfo-feign-client",
    url = "https://openapi.naver.com/v1/nid/me"
)
interface NaverUserInfoFeignClient {
    @GetMapping
    fun getUserInfo(
        @RequestHeader("Authorization") accessToken: String
    ): NaverInfoResponse
}
