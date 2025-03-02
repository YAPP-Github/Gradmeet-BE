package com.dobby.backend.infrastructure.gateway.cache

import com.dobby.domain.gateway.CacheGateway
import org.springframework.core.env.Environment
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisCacheGatewayImpl(
    private val redisTemplate: RedisTemplate<String, String>,
    private val environment: Environment
) : CacheGateway {

    private val cacheTimeout = 240L
    private val codeTimeout = 10L
    private val requestTimeout = 24L

    override fun get(key: String): String? {
        return redisTemplate.opsForValue().get(getCacheKey(key))
    }

    override fun set(key: String, value: String) {
        redisTemplate.opsForValue().set(getCacheKey(key), value, cacheTimeout, TimeUnit.MINUTES)
    }

    override fun setCode(key: String, value: String) {
        redisTemplate.opsForValue().set(getCacheKey(key), value, codeTimeout, TimeUnit.MINUTES)
    }

    override fun evict(key: String) {
        redisTemplate.delete(getCacheKey(key))
    }

    override fun incrementRequestCount(key: String) {
        val cacheKey = getCacheKey(key)
        val count = redisTemplate.opsForValue().increment(cacheKey, 1) ?: 1

        if(count == 1L){
            redisTemplate.expire(cacheKey, requestTimeout, TimeUnit.HOURS)
        }
    }

    private fun getCacheKey(key: String): String {
        val activeProfile = environment.activeProfiles.firstOrNull() ?: "local"
        return "$activeProfile:$key"
    }
}
