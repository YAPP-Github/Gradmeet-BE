package com.dobby.backend.infrastructure.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cloud.aws")
data class S3Properties(
    val s3: S3,
    val region: Region,
    val credentials: Credentials
) {
    data class S3(
        val bucket: String
    )

    data class Region(
        val static: String
    )

    data class Credentials(
        val accessKey: String,
        val secretKey: String
    )
}
