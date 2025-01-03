package com.dobby.backend

<<<<<<< HEAD
<<<<<<< HEAD

import com.dobby.backend.domain.usecase.UseCase
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
<<<<<<< HEAD
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
=======
=======
import com.dobby.backend.domain.usecase.UseCase
=======
import com.dobby.backend.application.usecase.UseCase
>>>>>>> b4241af (refact: move usecase file to application)
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
<<<<<<< HEAD
>>>>>>> ce9b9a9 (feat: add @ComponentScan to include UseCase beans in application context)
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.cloud.openfeign.EnableFeignClients
>>>>>>> dc4d52e ([YS-31] feat: 구글 OAuth 로그인 구현 (#13))
=======
>>>>>>> d02399a (fix: fix conflicts for merge)

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
	runApplication<DobbyBackendApplication>(*args)
}
