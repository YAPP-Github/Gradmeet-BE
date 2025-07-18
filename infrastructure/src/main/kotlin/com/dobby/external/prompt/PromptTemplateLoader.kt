package com.dobby.external.prompt

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component

@Component
class PromptTemplateLoader {

    private val mapper = jacksonObjectMapper()

    fun loadPrompt(resourcePath: String): PromptTemplate {
        val resource = this::class.java.classLoader.getResource(resourcePath)
            ?: throw IllegalArgumentException("프롬프트 파일을 찾을 수 없습니다: $resourcePath")

        return resource.openStream().use { inputStream ->
            mapper.readValue(inputStream, PromptTemplate::class.java)
        }
    }
}
