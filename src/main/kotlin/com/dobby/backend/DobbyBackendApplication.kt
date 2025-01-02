package com.dobby.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
class DobbyBackendApplication

fun main(args: Array<String>) {
	val dotenv = Dotenv.configure().load()
	dotenv.entries().forEach{ entry ->
		System.setProperty(entry.key, entry.value)
	}
	runApplication<DobbyBackendApplication>(*args)
}
