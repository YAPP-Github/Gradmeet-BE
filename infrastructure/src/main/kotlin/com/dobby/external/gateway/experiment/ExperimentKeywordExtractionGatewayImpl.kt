package com.dobby.external.gateway.experiment

import com.dobby.api.dto.request.OpenAiRequest
import com.dobby.external.feign.openAi.OpenAiFeignClient
import com.dobby.external.prompt.ExperimentPostKeywordDto
import com.dobby.external.prompt.ExperimentPostKeywordMapper
import com.dobby.external.prompt.PromptTemplate
import com.dobby.external.prompt.PromptTemplateLoader
import com.dobby.gateway.experiment.ExperimentKeywordExtractionGateway
import com.dobby.model.experiment.keyword.ExperimentPostKeyword
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service

@Service
class ExperimentKeywordExtractionGatewayImpl(
    private val openAiFeignClient: OpenAiFeignClient,
    private val promptTemplateLoader: PromptTemplateLoader,
    private val mapper: ExperimentPostKeywordMapper
) : ExperimentKeywordExtractionGateway {

    private val promptTemplate: PromptTemplate by lazy {
        promptTemplateLoader.loadPrompt("prompts/keyword_extraction_prompt.json")
    }

    override fun extractKeywords(text: String): ExperimentPostKeyword {
        val objectMapper = jacksonObjectMapper()
        val promptJson = objectMapper.writeValueAsString(promptTemplate)
        val prompt = promptJson.replace("{{text}}", text)

        val messages = listOf(
            OpenAiRequest.Message(role = "user", content = prompt)
        )

        val request = OpenAiRequest(
            model = "gpt-4o",
            temperature = 0.2,
            messages = messages
        )

        val response = openAiFeignClient.chatCompletion(request)
        val content = response.choices.firstOrNull()?.message?.content
            ?: throw IllegalStateException("OpenAI 응답 없음")

        return try {
            // 마크다운 코드 블록 제거 및 JSON 정리
            val cleanedContent = cleanJsonResponse(content)
            val dto = objectMapper.readValue<ExperimentPostKeywordDto>(cleanedContent)
            mapper.toDomain(dto)
        } catch (e: Exception) {
            throw IllegalStateException("응답 파싱 실패: $content", e)
        }
    }

    private fun cleanJsonResponse(content: String): String {
        return content.trim()
            // 마크다운 코드 블록 제거
            .replace(Regex("^```json\\s*"), "")
            .replace(Regex("```\\s*$"), "")
            // 시작/끝 백틱 제거
            .replace(Regex("^`+"), "")
            .replace(Regex("`+$"), "")
            .trim()
    }
}
