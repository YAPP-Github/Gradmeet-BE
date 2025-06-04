package com.dobby.validator

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RedirectUriValidator(
    @Value("\${oauth.google.redirectUris}")
    private val allowedGoogleRedirectUris: List<String>
) {
    fun isValidGoogleRedirectUri(uri: String): Boolean = allowedGoogleRedirectUris.contains(uri)
}
