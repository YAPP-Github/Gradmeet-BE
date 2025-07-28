package com.dobby.converter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component

@Component
class JsonConverter {
    private val objectMapper = jacksonObjectMapper()

    fun <T> toJson(obj: T): String = objectMapper.writeValueAsString(obj)

    fun <T> fromJson(json: String, clazz: Class<T>): T = objectMapper.readValue(json, clazz)
}
