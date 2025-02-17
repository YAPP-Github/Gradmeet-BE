package com.dobby.backend.application.aop

import com.dobby.backend.application.common.LockKeyProvider
import com.dobby.backend.infrastructure.lock.RedisLockManager
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Aspect
@Component
class DistributedLockAspect(
    private val redisLockManager: RedisLockManager,
    private val environment: Environment,
    private val aopForNewTransaction: AopForNewTransaction
) {
    private val logger: Logger = LoggerFactory.getLogger(DistributedLockAspect::class.java)

    @Around("@annotation(distributedLock)")
    fun around(joinPoint: ProceedingJoinPoint, distributedLock: DistributedLock): Any? {
        val args = joinPoint.args
        val lockKeyProvider = args.find { it is LockKeyProvider } as? LockKeyProvider
            ?: throw IllegalArgumentException("Must implement LockKeyProvider")

        val activeProfile = environment.activeProfiles.firstOrNull() ?: "local"
        val lockKey = "$activeProfile:${distributedLock.keyPrefix}:${lockKeyProvider.getLockKey()}"

        return executeWithLock(joinPoint, lockKey)
    }

    private fun executeWithLock(joinPoint: ProceedingJoinPoint, lockKey: String): Any? {
        logger.info("Trying to acquire lock for key: {}", lockKey)

        val lockValue = redisLockManager.lock(lockKey)
        if (lockValue == null) {
            logger.error("Lock acquisition failed immediately for key: {}", lockKey)
            return null
        }

        logger.info("Lock acquired successfully for key: {}", lockKey)
        return try {
            aopForNewTransaction.callNewTransaction {
                joinPoint.proceed()
            }
        } finally {
            if (redisLockManager.isLocked(lockKey)) {
                redisLockManager.unlock(lockKey, lockValue)
                logger.info("Lock released for key: {}", lockKey)
            } else {
                logger.warn("Lock key {} was already released or expired before unlock", lockKey)
            }
        }
    }
}
