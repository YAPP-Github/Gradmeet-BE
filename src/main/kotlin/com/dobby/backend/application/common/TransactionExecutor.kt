package com.dobby.backend.application.common

interface TransactionExecutor {
    suspend fun <T> execute(block: suspend () -> T): T
}

