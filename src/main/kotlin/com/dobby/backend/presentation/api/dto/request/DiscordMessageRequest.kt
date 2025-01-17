package com.dobby.backend.presentation.api.dto.request

data class DiscordMessageRequest(
    val content: String? = null,
    val embeds: List<Embed>? = null
) {

    data class Embed(
        val title: String? = null,
        val description: String? = null
    )
}
