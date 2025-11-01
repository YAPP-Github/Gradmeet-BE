package com.dobby.external.feign.discord

import com.dobby.api.dto.request.DiscordMessageRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "discord-alarm-feign-client",
    url = "https://discord.com"
)
interface DiscordFeignClient {

    @PostMapping(
        value = ["/api/webhooks/{id}/{token}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun sendMessage(
        @PathVariable id: String,
        @PathVariable token: String,
        @RequestBody discordMessageRequest: DiscordMessageRequest
    )
}
