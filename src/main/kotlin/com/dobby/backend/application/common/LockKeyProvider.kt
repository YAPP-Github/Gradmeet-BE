package com.dobby.backend.application.common

interface LockKeyProvider {
    fun getLockKey(): String
}
