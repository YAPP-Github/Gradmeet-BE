package com.dobby.gateway

interface CacheGateway {
    fun <T> getObject(key: String, clazz: Class<T>): T?
    fun get(key: String): String?
    fun getAutoComplete(key: String): List<String>?
    fun setObject(key: String, value: Any)
    fun set(key: String, value: String)
    fun setCode(key: String, value: String)
    fun setAutoComplete(key: String, value: List<String>)
    fun incrementRequestCount(key: String)
    fun evict(key: String)
}
