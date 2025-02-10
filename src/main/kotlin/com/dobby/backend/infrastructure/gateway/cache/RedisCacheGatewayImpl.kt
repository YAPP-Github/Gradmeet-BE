package com.dobby.backend.infrastructure.gateway.cache

import com.dobby.backend.domain.gateway.CacheGateway
import org.springframework.core.env.Environment
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisCacheGatewayImpl(
    private val redisTemplate: RedisTemplate<String, String>,
    private val environment: Environment
) : CacheGateway {

    private val cacheTimeout = 300L

    override fun get(key: String): String? {
        return redisTemplate.opsForValue().get(getCacheKey(key))
    }

    override fun set(key: String, value: String) {
        redisTemplate.opsForValue().set(getCacheKey(key), value, cacheTimeout, TimeUnit.MINUTES)
    }

    override fun evict(key: String) {
        redisTemplate.delete(getCacheKey(key))
    }

    private fun getCacheKey(key: String): String {
        val activeProfile = environment.activeProfiles.firstOrNull() ?: "local"
        return "$activeProfile:$key"
    }
}
