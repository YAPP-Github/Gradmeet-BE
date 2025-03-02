package com.dobby.backend.presentation.api.config.filter

import com.dobby.exception.AuthenticationTokenNotFoundException
import com.dobby.exception.AuthenticationTokenNotValidException
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val handlerExceptionResolver: HandlerExceptionResolver,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val authenticationHeader =
                request.getHeader("Authorization") ?: throw AuthenticationTokenNotFoundException
            val accessToken = if (authenticationHeader.startsWith("Bearer "))
                authenticationHeader.substring(7)
            else throw AuthenticationTokenNotValidException

            val authentication = jwtTokenProvider.parseAuthentication(accessToken)
            SecurityContextHolder.getContext().authentication = authentication
            return filterChain.doFilter(request, response)
        } catch (e: Exception) {
            handlerExceptionResolver.resolveException(request, response, null, e)
        }
    }
}
