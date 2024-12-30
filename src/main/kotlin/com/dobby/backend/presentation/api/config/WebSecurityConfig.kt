package com.dobby.backend.presentation.api.config

import com.dobby.backend.domain.exception.PermissionDeniedException
import com.dobby.backend.domain.exception.UnauthorizedException
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.presentation.api.config.filter.JwtAuthenticationFilter
import com.dobby.backend.presentation.api.config.handler.OauthSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.HandlerExceptionResolver

@Configuration
@EnableMethodSecurity
class WebSecurityConfig(private val oauthSuccessHandler: OauthSuccessHandler) {
    @Bean
    @Order(0)
    fun securityFilterChain(
        httpSecurity: HttpSecurity,
        jwtTokenProvider: JwtTokenProvider,
        handlerExceptionResolver: HandlerExceptionResolver,
    ): SecurityFilterChain = httpSecurity
        .securityMatcher( "/v1/members/**")
        .csrf { it.disable() }
        .cors(Customizer.withDefaults())
        .sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .authorizeHttpRequests {
            it.anyRequest().authenticated()
        }
        .addFilterBefore(
            JwtAuthenticationFilter(jwtTokenProvider, handlerExceptionResolver),
            UsernamePasswordAuthenticationFilter::class.java
        )
        .exceptionHandling {
            it.accessDeniedHandler { request, response, exception ->
                handlerExceptionResolver.resolveException(request, response, null, PermissionDeniedException())
            }.authenticationEntryPoint { request, response, authException ->
                handlerExceptionResolver.resolveException(request, response, null, UnauthorizedException())
            }
        }
        .build()

    @Bean
    @Order(1)
    fun authSecurityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain = httpSecurity
        .securityMatcher("/v1/auth/**")
        .csrf { it.disable() }
        .cors(Customizer.withDefaults())
        .sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .authorizeHttpRequests {
            it.requestMatchers("/v1/auth/oauth2/**").permitAll() //oauth 엔드포인트 허용
            it.anyRequest().permitAll()
        }
        .oauth2Login {
            it.successHandler(oauthSuccessHandler)
        }
        .build()
}
