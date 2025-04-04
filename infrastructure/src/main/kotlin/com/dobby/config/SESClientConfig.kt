package com.dobby.config

import com.dobby.config.properties.SESProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ses.SesAsyncClient

@Configuration
class SESClientConfig(
    private val sesProperties: SESProperties
) {
    @Bean
    fun sesAsyncClient(): SesAsyncClient {
        return SesAsyncClient.builder()
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
