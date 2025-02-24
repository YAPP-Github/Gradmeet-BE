package com.dobby.backend.infrastructure.logging

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object SensitiveDataMasker {
    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    private val maskPatterns = mapOf(
        "oauthEmail" to { value: String -> value.replaceBefore("@", "*****") },
        "univEmail" to { value: String -> value.replaceBefore("@", "*****") },
        "contactEmail" to { value: String -> value.replaceBefore("@", "*****") },
        "labInfo" to { _: String -> "*****" }
    )

    /**
     * 특정 키워드가 포함된 민감 정보를 마스킹 처리
     */
    fun mask(data: Any?): Any? {
        if (data == null) return null

        return try {
            val jsonString = objectMapper.writeValueAsString(data)
            var maskedString = jsonString
            for ((key, maskFunction) in maskPatterns) {
                maskedString = maskedString.replace(Regex("(?<=\"$key\":\\s?\")[^\"]+")) {
                    maskFunction(it.value)
                }
            }

            objectMapper.readValue<Map<String, Any>>(maskedString)
        } catch (e: Exception) {
            maskPatterns.mapValues { it.value }
        }
    }
}
