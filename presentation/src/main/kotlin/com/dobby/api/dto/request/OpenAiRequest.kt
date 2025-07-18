package com.dobby.api.dto.request

data class OpenAiRequest(
    val model: String,
    val temperature: Double,
    val messages: List<Message>
) {
    data class Message(
        val role: String,
        val content: String
    )
}
