package com.dobby.gateway

interface CacheGateway {
    fun <T> getObject(key: String, clazz: Class<T>): T?
    fun get(key: String): String?
    fun setObject(key: String, value: Any)
    fun set(key: String, value: String)
    fun setCode(key: String, value: String)
    fun incrementRequestCount(key: String)
    fun evict(key: String)
}
