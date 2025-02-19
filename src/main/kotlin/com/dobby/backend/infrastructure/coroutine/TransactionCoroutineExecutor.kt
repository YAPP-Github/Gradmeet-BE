package com.dobby.backend.infrastructure.coroutine

import com.dobby.backend.application.common.TransactionExecutor
import kotlinx.coroutines.*
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate
import java.lang.RuntimeException

@Component
class TransactionCoroutineExecutor(
    private val transactionTemplate: TransactionTemplate,
    private val coroutineScope: CoroutineScope
): TransactionExecutor {

    override suspend fun <T> execute(block: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            val deferred = coroutineScope.async {
                transactionTemplate.execute {
                    runBlocking { block() }
                } ?: throw RuntimeException("Transaction Failed")
            }
            deferred.await()
        }
    }
}
