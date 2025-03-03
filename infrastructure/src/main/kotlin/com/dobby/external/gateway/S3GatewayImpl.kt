package com.dobby.external.gateway

import com.dobby.s3.S3PreSignedUrlProvider
import org.springframework.stereotype.Component

@Component
class S3GatewayImpl(
    private val s3PreSignedUrlProvider: S3PreSignedUrlProvider
) : S3Gateway {

    override fun getExperimentPostPreSignedUrl(fileName: String): String {
        return s3PreSignedUrlProvider.getExperimentPostPreSignedUrl(fileName)
    }
}
