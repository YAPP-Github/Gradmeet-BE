package com.dobby.external.gateway.discord

import com.dobby.api.dto.request.DiscordMessageRequest
import com.dobby.config.properties.DiscordProperties
import com.dobby.external.feign.discord.DiscordFeignClient
import com.dobby.gateway.AlertChannel
import com.dobby.gateway.AlertGateway
import org.springframework.stereotype.Component
import java.io.PrintWriter
import java.io.StringWriter
import java.time.LocalDateTime

@Component
class AlertGatewayImpl(
    private val discordProperties: DiscordProperties,
    private val discordFeignClient: DiscordFeignClient
) : AlertGateway {

    fun send(channel: AlertChannel, body: DiscordMessageRequest) {
        val key = when (channel) {
            AlertChannel.ERRORS -> "errors"
            AlertChannel.NOTIFY -> "notify"
        }
        val hook = discordProperties.webhooks[key] ?: return
        runCatching {
            discordFeignClient.sendMessage(hook.id, hook.token, body)
        }.onFailure {
        }
    }

    override fun sendError(e: Exception, requestUrl: String, clientIp: String?) {
        send(AlertChannel.ERRORS, createErrorMessage(e, requestUrl, clientIp))
    }

    override fun sendNotify(
        title: String,
        description: String,
        content: String?
    ) {
        send(AlertChannel.NOTIFY, createNotifyMessage(title, description, content))
    }

    private fun createErrorMessage(e: Exception, requestUrl: String, clientIp: String?): DiscordMessageRequest {
        return DiscordMessageRequest(
            content = "# 🚨 에러 발생 비이이이이사아아아앙",
            embeds = listOf(
                DiscordMessageRequest.Embed(
                    title = "ℹ️ 에러 정보",
                    description = """
                        ### 🕖 발생 시간
                        ${LocalDateTime.now()}

                        ### 🔗 요청 URL
                        $requestUrl

                        ### 🖥️ 클라이언트 IP
                        ${clientIp ?: "알 수 없음"}

                        ### 📄 Stack Trace
                        ```
                        ${getStackTrace(e).substring(0, 1000)}
                        ```
                    """.trimIndent()
                )
            )
        )
    }
    fun createNotifyMessage(
        title: String,
        description: String,
        content: String? = null
    ): DiscordMessageRequest {
        val embeds = listOf(
            DiscordMessageRequest.Embed(
                title = "📣 $title",
                description = description
            )
        )
        return DiscordMessageRequest(
            content = content,
            embeds = embeds
        )
    }

    private fun getStackTrace(e: Exception): String {
        val stringWriter = StringWriter()
        e.printStackTrace(PrintWriter(stringWriter))
        return stringWriter.toString()
    }
}
