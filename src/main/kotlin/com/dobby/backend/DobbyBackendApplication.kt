package com.dobby.backend

import com.dobby.backend.application.usecase.UseCase
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@ComponentScan(
	includeFilters = [ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = [UseCase::class]
	)]
)
@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.dobby.backend.infrastructure.database.repository"])
@ConfigurationPropertiesScan
@EnableFeignClients
class DobbyBackendApplication

fun main(args: Array<String>) {
	runApplication<DobbyBackendApplication>(*args)
}
