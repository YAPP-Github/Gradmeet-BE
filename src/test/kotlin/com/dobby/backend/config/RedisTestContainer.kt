package com.dobby.backend.config

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

object RedisTestContainer : TestListener {

    private val redis: GenericContainer<*> = GenericContainer(DockerImageName.parse("redis:7.0.8-alpine"))
        .apply {
            withExposedPorts(6379)
            portBindings = listOf("6379:6379")
            withReuse(true)
        }

    override suspend fun beforeSpec(spec: Spec) {
        redis.start()

        val redisHost = "127.0.0.1"
        val redisPort = "6379"

        System.setProperty("spring.data.redis.host", redisHost)
        System.setProperty("spring.data.redis.port", redisPort)
    }

    override suspend fun afterSpec(spec: Spec) {
        redis.stop()
    }
}
