package com.dobby.backend.infrastructure.gateway.discord

import com.dobby.api.dto.request.DiscordMessageRequest
import com.dobby.gateway.AlertGateway
import com.dobby.backend.infrastructure.feign.discord.DiscordFeignClient
import org.springframework.stereotype.Component
import java.io.PrintWriter
import java.io.StringWriter
import java.time.LocalDateTime

@Component
class AlertGatewayImpl(
    private val discordFeignClient: DiscordFeignClient
) : AlertGateway {

    override fun sendError(e: Exception, requestUrl: String, clientIp: String?) {
        sendMessage(createMessage(e, requestUrl, clientIp))
    }

    private fun createMessage(e: Exception, requestUrl: String, clientIp: String?): DiscordMessageRequest {
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

    private fun getStackTrace(e: Exception): String {
        val stringWriter = StringWriter()
        e.printStackTrace(PrintWriter(stringWriter))
        return stringWriter.toString()
    }

    private fun sendMessage(request: DiscordMessageRequest) {
        discordFeignClient.sendMessage(request)
    }
}
