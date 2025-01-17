package com.dobby.backend.infrastructure.gateway.discord

import com.dobby.backend.domain.gateway.AlertGateway
import com.dobby.backend.infrastructure.feign.discord.DiscordFeignClient
import com.dobby.backend.presentation.api.dto.request.DiscordMessage
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import java.io.PrintWriter
import java.io.StringWriter
import java.time.LocalDateTime

@Component
class AlertGatewayImpl(
    private val discordFeignClient: DiscordFeignClient
): AlertGateway {

    override fun sendError(e: Exception, request: HttpServletRequest) {
        sendMessage(createMessage(e, request))
    }

    private fun createMessage(e: Exception, request: HttpServletRequest): DiscordMessage {
        return DiscordMessage(
            content = "# 🚨 에러 발생 비이이이이사아아아앙",
            embeds = listOf(
                DiscordMessage.Embed(
                    title = "ℹ️ 에러 정보",
                    description = """
                        ### 🕖 발생 시간
                        ${LocalDateTime.now()}
                        
                        ### 🔗 요청 URL
                        ${createRequestFullPath(request)}
                        
                        ### 📄 Stack Trace
                        ```
                        ${getStackTrace(e).substring(0, 1000)}
                        ```
                    """.trimIndent()
                )
            )
        )
    }

    private fun createRequestFullPath(request: HttpServletRequest): String {
        var fullPath = "${request.method} ${request.requestURL}"

        val queryString = request.queryString
        if (queryString != null) {
            fullPath += "?$queryString"
        }

        return fullPath
    }

    private fun getStackTrace(e: Exception): String {
        val stringWriter = StringWriter()
        e.printStackTrace(PrintWriter(stringWriter))
        return stringWriter.toString()
    }

    private fun sendMessage(request: DiscordMessage) {
        discordFeignClient.sendMessage(request)
    }
}
