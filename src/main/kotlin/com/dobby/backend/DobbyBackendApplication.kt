package com.dobby.backend


import com.dobby.backend.domain.usecase.UseCase
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
<<<<<<< HEAD
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
=======
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.cloud.openfeign.EnableFeignClients
>>>>>>> dc4d52e ([YS-31] feat: 구글 OAuth 로그인 구현 (#13))

@ComponentScan(
	includeFilters = [ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = [UseCase::class]
	)]
)
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableFeignClients
class DobbyBackendApplication

fun main(args: Array<String>) {
	val dotenv = Dotenv.configure().load()
	dotenv.entries().forEach{ entry ->
		System.setProperty(entry.key, entry.value)
	}
	runApplication<DobbyBackendApplication>(*args)
}
