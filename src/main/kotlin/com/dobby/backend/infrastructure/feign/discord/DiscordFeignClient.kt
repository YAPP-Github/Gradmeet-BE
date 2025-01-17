package com.dobby.backend.infrastructure.feign.discord

import com.dobby.backend.presentation.api.dto.request.DiscordMessage
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "discord-alarm-feign-client",
    url = "\${discord.webhook-url}"
)
interface DiscordFeignClient {

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun sendMessage(@RequestBody discordMessage: DiscordMessage)
}
