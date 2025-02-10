package com.dobby.backend.config

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

object RedisTestContainer : TestListener {

    private val redis: GenericContainer<*> = GenericContainer(DockerImageName.parse("redis:7.0.8-alpine"))
        .apply {
            withExposedPorts(6379)
            withReuse(true)
        }

    override suspend fun beforeSpec(spec: Spec) {
        redis.start()

        val redisHost = redis.host
        val redisPort = redis.firstMappedPort.toString()

        System.setProperty("spring.data.redis.host", redisHost)
        System.setProperty("spring.data.redis.port", redisPort)
    }

    override suspend fun afterSpec(spec: Spec) {
        System.clearProperty("spring.data.redis.host")
        System.clearProperty("spring.data.redis.port")
        redis.stop()
    }
}
