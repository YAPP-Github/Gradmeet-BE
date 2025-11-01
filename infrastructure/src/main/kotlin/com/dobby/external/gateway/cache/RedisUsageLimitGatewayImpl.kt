package com.dobby.external.gateway.cache

import com.dobby.gateway.UsageLimitGateway
import com.dobby.model.UsageSnapshot
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

@Component
class RedisUsageLimitGatewayImpl(
    private val redisTemplate: RedisTemplate<String, String>,
    private val environment: Environment
) : UsageLimitGateway {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val zone = ZoneId.of("Asia/Seoul")

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

    override fun getCurrentUsage(memberId: String, dailyLimit: Int): UsageSnapshot {
        val key = getCacheKey(memberId)
        val count = (redisTemplate.opsForValue().get(key)?.toLong()) ?: 0L

        val ttlSeconds = redisTemplate.getExpire(key, TimeUnit.SECONDS)
        val resetsAt: LocalDateTime = when {
            ttlSeconds != null && ttlSeconds > 0L ->
                ZonedDateTime.now(zone).plusSeconds(ttlSeconds).toLocalDateTime()
            else ->
                nextMidnightKST().toLocalDateTime()
        }

        val remainingCount = maxOf(0L, dailyLimit.toLong() - count)
        return UsageSnapshot(
            count = count,
            limit = dailyLimit,
            remainingCount = remainingCount,
            resetsAt = resetsAt
        )
    }

    private fun getCacheKey(memberId: String): String {
        val activeProfile = environment.activeProfiles.firstOrNull() ?: "local"
        return "$activeProfile:usage:$memberId:${LocalDate.now()}"
    }

    private fun nextMidnightKST(): ZonedDateTime {
        val today = ZonedDateTime.now(zone).toLocalDate()
        return today.plusDays(1).atStartOfDay(zone)
    }
}
