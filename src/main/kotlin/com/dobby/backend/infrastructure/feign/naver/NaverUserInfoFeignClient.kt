package com.dobby.backend.infrastructure.feign.naver

import com.dobby.backend.presentation.api.dto.response.auth.NaverInfoResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "naver-userinfo-feign-client",
    url = "https://openapi.naver.com/v1/nid/me"
)
interface NaverUserInfoFeignClient {
    @PostMapping
    fun getUserInfo(@RequestHeader("Authorization") accessToken: String): NaverInfoResponse
}
