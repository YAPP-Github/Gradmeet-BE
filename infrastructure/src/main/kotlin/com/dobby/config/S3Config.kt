package com.dobby.config

import com.dobby.config.properties.S3Properties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.presigner.S3Presigner

@Configuration
class S3Config(
    private val properties: S3Properties
) {

    @Bean
    fun s3Presigner(): S3Presigner {
        val awsCredentialsProvider = StaticCredentialsProvider.create(
            AwsBasicCredentials.create(properties.credentials.accessKey, properties.credentials.secretKey)
        )
        return S3Presigner.builder()
            .region(Region.AP_NORTHEAST_2)
            .credentialsProvider(awsCredentialsProvider)
            .build()
    }
}
