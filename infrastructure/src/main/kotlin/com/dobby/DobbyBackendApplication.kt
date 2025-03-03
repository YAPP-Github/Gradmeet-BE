package com.dobby

import com.dobby.usecase.UseCase
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.FilterType

@ComponentScan(
	includeFilters = [ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = [UseCase::class]
	)]
)
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableFeignClients
@EnableAspectJAutoProxy
class DobbyBackendApplication

fun main(args: Array<String>) {
	runApplication<DobbyBackendApplication>(*args)
}
