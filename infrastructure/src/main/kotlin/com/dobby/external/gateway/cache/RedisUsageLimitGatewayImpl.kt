package com.dobby.external.gateway.cache

import com.dobby.gateway.UsageLimitGateway
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Component
class RedisUsageLimitGatewayImpl(
    private val redisTemplate: RedisTemplate<String, String>,
    private val environment: Environment
) : UsageLimitGateway {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun incrementAndCheckLimit(memberId: String, dailyLimit: Int): Boolean {
        val key = getCacheKey(memberId)
        val count = redisTemplate.opsForValue().increment(key, 1) ?: 1L

        if (count == 1L) {
            val expireSeconds = Duration.between(LocalDateTime.now(), LocalDate.now().plusDays(1).atStartOfDay()).seconds
            redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS)
        }

        logger.debug("Usage count for key=$key is $count")

        return count <= dailyLimit
    }

    private fun getCacheKey(memberId: String): String {
        val activeProfile = environment.activeProfiles.firstOrNull() ?: "local"
        return "$activeProfile:usage:$memberId:${LocalDate.now()}"
    }
}
