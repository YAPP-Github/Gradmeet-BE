package com.dobby.backend.infrastructure.lock

import com.dobby.backend.infrastructure.identifier.TsidIdGenerator
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisLockManager(
    private val redisTemplate: StringRedisTemplate,
    private val tsidIdGenerator: TsidIdGenerator
) {
    private val logger = LoggerFactory.getLogger(RedisLockManager::class.java)

    fun lock(key: String, expireTime: Long = 5L): String? {
        val lockValue = tsidIdGenerator.generateId()
        val success = redisTemplate.opsForValue().setIfAbsent(key, lockValue, expireTime, TimeUnit.SECONDS)

        return if (success == true) lockValue else null
    }

    fun unlock(key: String, lockValue: String) {
        val script = """
            if redis.call("get", KEYS[1]) == ARGV[1] then
                return redis.call("del", KEYS[1])
            else
                return 0
            end
        """.trimIndent()
        val result = redisTemplate.execute(
            RedisScript.of(script, Long::class.java),
            listOf(key),
            lockValue
        )
        if (result == 1L) {
            logger.info("Lock released successfully for key: {}", key)
        } else {
            logger.warn("Unlock failed: Lock value mismatch for key {}", key)
        }
    }

    fun isLocked(key: String): Boolean {
        return redisTemplate.opsForValue().get(key) != null
    }
}
