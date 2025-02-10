package com.dobby.backend.config

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

object RedisTestContainer : TestListener {
    private val isCI = System.getenv("GITHUB_ACTIONS") == "true"

    private val redis: GenericContainer<*> = GenericContainer(DockerImageName.parse("redis:7.0.8-alpine")).apply {
        withExposedPorts(6379)

        if (!isCI) {
            portBindings = listOf("6379:6379")
        }
    }

    override suspend fun beforeSpec(spec: Spec) {
        redis.start()

        val redisHost = "127.0.0.1"
        val redisPort = redis.getMappedPort(6379).toString()

        System.setProperty("spring.data.redis.host", redisHost)
        System.setProperty("spring.data.redis.port", redisPort)
    }

    override suspend fun afterSpec(spec: Spec) {
        redis.stop()
    }
}
