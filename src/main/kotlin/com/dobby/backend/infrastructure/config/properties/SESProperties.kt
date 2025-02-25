package com.dobby.backend.infrastructure.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "aws.ses")
data class SESProperties (
    val email: Email,
    val region: Region,
    val credentials: Credentials
) {
    data class Email(
        val sender: String
    )

    data class Region(
        val static: String
    )

    data class Credentials(
        val accessKey: String,
        val secretKey: String
    )
}
