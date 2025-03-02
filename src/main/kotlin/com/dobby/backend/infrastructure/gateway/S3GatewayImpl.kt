package com.dobby.backend.infrastructure.gateway

import com.dobby.gateway.S3Gateway
import com.dobby.backend.infrastructure.s3.S3PreSignedUrlProvider
import org.springframework.stereotype.Component

@Component
class S3GatewayImpl(
    private val s3PreSignedUrlProvider: S3PreSignedUrlProvider
) : S3Gateway {

    override fun getExperimentPostPreSignedUrl(fileName: String): String {
        return s3PreSignedUrlProvider.getExperimentPostPreSignedUrl(fileName)
    }
}
