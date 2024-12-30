package com.dobby.backend.presentation.api.controller

import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType.*
import com.dobby.backend.presentation.api.dto.request.OauthUserDto
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/oauth")
class OauthController {
    @GetMapping("/login")
    fun getUserDetails(authentication: OAuth2AuthenticationToken) : OauthUserDto {
        val userAttributes = authentication.principal.attributes
        val email = userAttributes["email"] as String
        val name = userAttributes["name"] as String

        return OauthUserDto(
            email = email,
            name = name,
            provider = GOOGLE
        )
    }
}
