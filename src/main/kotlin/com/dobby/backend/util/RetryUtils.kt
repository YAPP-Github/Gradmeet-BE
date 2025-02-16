package com.dobby.backend.util

import com.dobby.backend.domain.exception.EmailAlreadyVerifiedException
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import kotlin.math.pow
import kotlin.random.Random

object RetryUtils {
    private val logger = LoggerFactory.getLogger(RetryUtils::class.java)

    suspend fun <T> retryWithBackOff(
        maxRetries: Int = 3,
        defaultDelay: Long = 1000L,
        maxJitter: Long = 500L,
        block: suspend() -> T
    ): T {
        var curAttempt = 1
        while(true){
            try {
                return block()
            } catch (e: EmailAlreadyVerifiedException) {
                throw e
            } catch (ex: Exception) {
                if (curAttempt >= maxRetries) {
                    logger.error("Operation failed after $maxRetries attmpets.", ex)
                    throw ex
                }
                val backOffTime = calculateBackOff(curAttempt, defaultDelay, maxJitter)
                logger.warn("Retrying... Current Attempt: $curAttempt, Waiting: ${backOffTime}ms")
                delay(backOffTime)
                curAttempt +=1
            }
        }
    }

    private fun calculateBackOff(curAttempt: Int, defaultDelay: Long, maxJitter: Long): Long {
        return defaultDelay * (2.0.pow(curAttempt)).toLong() + Random.nextLong(0, maxJitter)
    }
}
