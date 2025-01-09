package com.dobby.backend

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.infrastructure.config.properties.GoogleAuthProperties
import com.dobby.backend.infrastructure.config.properties.NaverAuthProperties
import com.dobby.backend.infrastructure.config.properties.S3Properties
import com.dobby.backend.infrastructure.config.properties.TokenProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@ComponentScan(
	includeFilters = [ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = [UseCase::class]
	)]
)
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties(TokenProperties::class, S3Properties::class, GoogleAuthProperties::class, NaverAuthProperties::class)
@EnableFeignClients
@EnableJpaAuditing
class DobbyBackendApplication

fun main(args: Array<String>) {
	runApplication<DobbyBackendApplication>(*args)
}
