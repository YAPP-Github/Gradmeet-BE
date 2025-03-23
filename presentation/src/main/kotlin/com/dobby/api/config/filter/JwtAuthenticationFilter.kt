package com.dobby.api.config.filter

import com.dobby.exception.AuthenticationTokenNotFoundException
import com.dobby.exception.AuthenticationTokenNotValidException
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

class JwtAuthenticationFilter(
    private val securityManager: SecurityManager,
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

            val securityUser = securityManager.parseAuthentication(accessToken)
            val authentication = createAuthentication(securityUser)

            SecurityContextHolder.getContext().authentication = authentication
            return filterChain.doFilter(request, response)
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
