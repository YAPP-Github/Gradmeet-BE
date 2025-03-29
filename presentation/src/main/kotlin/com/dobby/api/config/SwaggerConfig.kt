package com.dobby.api.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    servers = [
        Server(
            url = "\${swagger.server-url}",
            description = "Default Server URL"
        )
    ],
    info = Info(
        title = "그라밋 백엔드 API 명세",
        description = "그라밋 Swagger 문서입니다.",
        version = "1.0",
        contact = Contact(name = "깃허브 주소", url = "https://github.com/YAPP-Github/25th-Web-Team-2-BE/")
    )
)
@Configuration
class SwaggerConfig {
    val googleAuthUrl = "https://accounts.google.com/o/oauth2/auth"
    val googleTokenUrl = "https://oauth2.googleapis.com/token"

    val jwtScheme = SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("Bearer")
        .bearerFormat("JWT")

    val googleOAuthScheme = SecurityScheme()
        .type(SecurityScheme.Type.OAUTH2)
        .flows(
            OAuthFlows().authorizationCode(
                OAuthFlow()
                    .authorizationUrl(googleAuthUrl)
                    .tokenUrl(googleTokenUrl)
                    .scopes(
                        Scopes()
                            .addString("email", "Access your email address")
                            .addString("profile", "Access your profile information")
                    )
            )
        )

    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .addSecurityItem(SecurityRequirement().addList("JWT 토큰").addList("Google OAuth2 토큰"))
        .components(
            Components()
                .addSecuritySchemes("JWT 토큰", jwtScheme)
                .addSecuritySchemes("Google OAuth2 토큰", googleOAuthScheme)
        )
}
