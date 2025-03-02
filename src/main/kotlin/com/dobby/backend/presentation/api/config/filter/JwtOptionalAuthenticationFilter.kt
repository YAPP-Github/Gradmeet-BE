package com.dobby.backend.presentation.api.config.filter

import com.dobby.exception.AuthenticationTokenNotValidException
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.token.JwtTokenManager
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

class JwtOptionalAuthenticationFilter(
    private val jwtTokenManager: JwtTokenManager,
    private val handlerExceptionResolver: HandlerExceptionResolver,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val authenticationHeader = request.getHeader("Authorization")

            if (authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
                val accessToken = if (authenticationHeader.startsWith("Bearer "))
                    authenticationHeader.substring(7)
                else throw AuthenticationTokenNotValidException

                val authentication = jwtTokenManager.parseAuthentication(accessToken)
                SecurityContextHolder.getContext().authentication = authentication
            }

            // 헤더가 없으면 인증 없이 진행
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            handlerExceptionResolver.resolveException(request, response, null, e)
        }
    }
}
