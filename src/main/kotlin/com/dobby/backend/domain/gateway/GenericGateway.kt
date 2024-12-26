package com.dobby.backend.domain.gateway

interface GenericGateway<T, ID> {
    fun save(entity: T): T
    fun findById(id: ID): T?
    fun findAll(): List<T>
    fun deleteById(id: ID)
}
