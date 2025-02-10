package com.dobby.backend.config

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

object RedisTestContainerConfig : BeforeAllCallback {

    private const val REDIS_IMAGE = "redis:7.0.8-alpine"
    private const val REDIS_PORT = 6379

    private val redisContainer: GenericContainer<out GenericContainer<*>> = GenericContainer(DockerImageName.parse(REDIS_IMAGE))
        .apply {
            withExposedPorts(REDIS_PORT)
            withReuse(true) // 컨테이너 재사용 설정
            if (!isRunning) start()
        }

    override fun beforeAll(context: ExtensionContext?) {
        System.setProperty("spring.data.redis.host", redisContainer.host)
        System.setProperty("spring.data.redis.port", redisContainer.getMappedPort(REDIS_PORT).toString())
    }
}
