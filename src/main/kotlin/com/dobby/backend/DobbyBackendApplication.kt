package com.dobby.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DobbyBackendApplication

fun main(args: Array<String>) {
	runApplication<DobbyBackendApplication>(*args)
}
