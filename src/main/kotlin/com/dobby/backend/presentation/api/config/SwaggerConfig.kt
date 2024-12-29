package com.dobby.backend.presentation.api.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    servers = [Server(
        url = "/",
        description = "Default Server URL"
    )],
    info = Info(
        title = "그라밋 백엔드 API 명세",
        description = "그라밋 Swagger 문서입니다.",
        version = "1.0",
        contact = Contact(name = "깃허브 주소", url = "https://github.com/YAPP-Github/25th-Web-Team-2-BE/")
    )
)
@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .addSecurityItem(SecurityRequirement().addList("JWT 토큰"))
        .components(
            Components().addSecuritySchemes("JWT 토큰",
            SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT")))
}
