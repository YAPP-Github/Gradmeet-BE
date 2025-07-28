package com.dobby.external.gateway.experiment

import com.dobby.api.dto.request.OpenAiRequest
import com.dobby.exception.CustomOpenAiCallException
import com.dobby.external.feign.openAi.OpenAiFeignClient
import com.dobby.external.prompt.ExperimentPostKeywordMapper
import com.dobby.external.prompt.PromptTemplate
import com.dobby.external.prompt.PromptTemplateLoader
import com.dobby.external.prompt.dto.ExperimentPostKeywordDto
import com.dobby.gateway.experiment.ExperimentKeywordExtractionGateway
import com.dobby.model.experiment.keyword.ExperimentPostKeyword
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import feign.FeignException
import org.springframework.stereotype.Component

@Component
class ExperimentKeywordExtractionGatewayImpl(
    private val openAiFeignClient: OpenAiFeignClient,
    private val promptTemplateLoader: PromptTemplateLoader,
    private val mapper: ExperimentPostKeywordMapper
) : ExperimentKeywordExtractionGateway {

    private val objectMapper = jacksonObjectMapper()

    private val promptTemplate: PromptTemplate by lazy {
        promptTemplateLoader.loadPrompt("prompts/keyword_extraction_prompt.json")
    }

    override fun extractKeywords(text: String): ExperimentPostKeyword {
        val promptJson = objectMapper.writeValueAsString(promptTemplate)
        val prompt = promptJson.replace("{{text}}", escapeJsonString(text))
        val messages = listOf(
            OpenAiRequest.Message(role = "user", content = prompt)
        )

        val request = OpenAiRequest(
            model = "gpt-4o",
            temperature = 0.2,
            messages = messages
        )

        val content = try {
            val response = openAiFeignClient.chatCompletion(request)
            response.choices.firstOrNull()?.message?.content
                ?: throw IllegalStateException("No response received from OpenAI")
        } catch (e: FeignException) {
            throw CustomOpenAiCallException("OpenAI API call failed (status=${e.status()})", e)
        } catch (e: Exception) {
            throw IllegalStateException("Unexpected error occurred during OpenAI API call", e)
        }

        return try {
            val cleanedContent = cleanJsonResponse(content)
            val dto = objectMapper.readValue<ExperimentPostKeywordDto>(cleanedContent)
            mapper.toDomain(dto)
        } catch (e: Exception) {
            throw IllegalStateException("Failed to parse response: $content", e)
        }
    }

    private fun escapeJsonString(text: String): String {
        return text.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }

    private fun cleanJsonResponse(content: String): String {
        return content.trim()
            .replace(Regex("^```json\\s*"), "")
            .replace(Regex("```\\s*$"), "")
            .replace(Regex("^`+"), "")
            .replace(Regex("`+$"), "")
            .trim()
    }
}
