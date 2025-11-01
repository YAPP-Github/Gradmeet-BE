package com.dobby.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties("discord")
data class DiscordProperties (
    val webhooks: Map<String, Webhook> = emptyMap()
) {
    data class Webhook(
        val id: String,
        val token: String
    )
}
