package com.dobby.backend.application.aop

interface LockKeyProvider {
    fun getLockKey(): String
}
