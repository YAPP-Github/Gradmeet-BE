package com.dobby.backend

import com.dobby.backend.infrastructure.config.TokenProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
class DobbyBackendApplication

fun main(args: Array<String>) {
	runApplication<DobbyBackendApplication>(*args)
}
