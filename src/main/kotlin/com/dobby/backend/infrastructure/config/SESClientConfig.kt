package com.dobby.backend.infrastructure.config

import com.dobby.backend.infrastructure.config.properties.SESProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ses.SesClient

@Configuration
class SESClientConfig(
    private val sesProperties: SESProperties
) {
    @Bean
    fun sesClient(): SesClient {
        return SesClient.builder()
            .region(Region.of(sesProperties.region.static))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        sesProperties.credentials.accessKey,
                        sesProperties.credentials.secretKey
                    )
                )
            )
            .build()
    }
}
