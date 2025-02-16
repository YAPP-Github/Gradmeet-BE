package com.dobby.backend.domain.gateway

interface CacheGateway {
    fun get(key: String): String?
    fun set(key: String, value: String)
    fun setCode(key: String, value: String)
    fun incrementRequestCount(key: String)
    fun evict(key: String)
}
