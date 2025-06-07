package com.dobby.auth

import org.springframework.stereotype.Component

@Component
class RedirectUriValidator(
    private val properties: GoogleRedirectUriProperties
) {
    fun isValidGoogleRedirectUri(uri: String): Boolean = properties.redirectUris.contains(uri)
}
