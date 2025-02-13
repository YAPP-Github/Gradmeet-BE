package com.dobby.backend.application.service

interface TransactionExecutor {
    suspend fun <T> execute(block: suspend () -> T): T
}

