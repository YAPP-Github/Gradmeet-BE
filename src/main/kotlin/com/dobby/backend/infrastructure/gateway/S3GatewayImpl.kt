package com.dobby.backend.infrastructure.gateway

import com.dobby.backend.domain.gateway.S3Gateway
import com.dobby.backend.infrastructure.s3.S3PreSignedUrlProvider
import com.dobby.backend.presentation.api.dto.response.PreSignedUrlResponse
import org.springframework.stereotype.Component

@Component
class S3GatewayImpl(
    private val s3PreSignedUrlProvider: S3PreSignedUrlProvider
) : S3Gateway {

    override fun getPreSignedUrl(fileName: String): PreSignedUrlResponse {
        return s3PreSignedUrlProvider.getPreSignedUrl(fileName)
    }
}
