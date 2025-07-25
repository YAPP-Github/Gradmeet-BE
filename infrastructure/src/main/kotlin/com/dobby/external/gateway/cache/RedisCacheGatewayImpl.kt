package com.dobby.external.gateway.cache

import com.dobby.gateway.CacheGateway
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisCacheGatewayImpl(
    private val redisTemplate: RedisTemplate<String, String>,
    private val environment: Environment
) : CacheGateway {

    private val objectMapper = jacksonObjectMapper()
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val cacheTimeout = 240L
    private val codeTimeout = 10L
    private val requestTimeout = 24L
    private val autoCompleteTimeout = 30L

    override fun <T> getObject(key: String, clazz: Class<T>): T? {
        return get(key)?.let { json ->
            objectMapper.readValue(json, clazz)
        }
    }

    override fun get(key: String): String? {
        return redisTemplate.opsForValue().get(getCacheKey(key))
    }

    override fun getAutoComplete(key: String): List<String>? {
        return try {
            get(key)?.let { json ->
                objectMapper.readValue(json, object : TypeReference<List<String>>() {})
            }
        } catch (e: Exception) {
            logger.warn("Failed to get autoComplete cache for key=$key", e)
            null
        }
    }

    override fun setObject(key: String, value: Any) {
        val jsonValue = objectMapper.writeValueAsString(value)
        set(key, jsonValue)
    }

    override fun set(key: String, value: String) {
        redisTemplate.opsForValue().set(getCacheKey(key), value, cacheTimeout, TimeUnit.MINUTES)
    }

    override fun setCode(key: String, value: String) {
        redisTemplate.opsForValue().set(getCacheKey(key), value, codeTimeout, TimeUnit.MINUTES)
    }

    override fun setAutoComplete(key: String, value: List<String>) {
        try {
            val json = objectMapper.writeValueAsString(value)
            redisTemplate.opsForValue().set(getCacheKey(key), json, autoCompleteTimeout, TimeUnit.MINUTES)
        } catch (e: Exception) {
            logger.warn("Failed to set autoComplete cache for key=$key", e)
        }
    }

    override fun evict(key: String) {
        redisTemplate.delete(getCacheKey(key))
    }

    override fun incrementRequestCount(key: String) {
        val cacheKey = getCacheKey(key)
        val count = redisTemplate.opsForValue().increment(cacheKey, 1) ?: 1

        if (count == 1L) {
            redisTemplate.expire(cacheKey, requestTimeout, TimeUnit.HOURS)
        }
    }

    private fun getCacheKey(key: String): String {
        val activeProfile = environment.activeProfiles.firstOrNull() ?: "local"
        return "$activeProfile:$key"
    }
}
