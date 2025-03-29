package com.dobby.api.config.filter

import com.dobby.security.SecurityManager
import com.dobby.security.SecurityUser
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

class JwtOptionalAuthenticationFilter(
    private val securityManager: SecurityManager,
    private val handlerExceptionResolver: HandlerExceptionResolver
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val authenticationHeader = request.getHeader("Authorization")

            if (authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
                val accessToken = authenticationHeader.substring(7)
                val securityUser = securityManager.parseAuthentication(accessToken)
                val authentication = createAuthentication(securityUser)
                SecurityContextHolder.getContext().authentication = authentication
            }

            // 헤더가 없으면 인증 없이 진행
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            handlerExceptionResolver.resolveException(request, response, null, e)
        }
    }

    private fun createAuthentication(securityUser: SecurityUser): Authentication {
        return UsernamePasswordAuthenticationToken(
            securityUser.memberId,
            null,
            securityUser.roles.map { SimpleGrantedAuthority(it) }
        )
    }
}
