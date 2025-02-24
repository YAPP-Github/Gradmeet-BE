package com.dobby.backend.infrastructure.logging

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object SensitiveDataMasker {
    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    private val maskPatterns: Map<String, (String) -> String> = mapOf(
        "name" to { value -> value.take(1) + "*****" },
        "oauthEmail" to { value -> value.first() + "*****@" + value.substringAfter("@") },
        "univEmail" to { value -> value.first() + "*****@" + value.substringAfter("@") },
        "contactEmail" to { value -> value.first() + "*****@" + value.substringAfter("@") },
        "univName" to { value -> value.take(1) + "*******" },
        "major" to { value -> value.take(1) + "********" },
        "labInfo" to { _ -> "*****" }
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
            maskPatterns.mapValues { it.value("*****") }
        }
    }
}
