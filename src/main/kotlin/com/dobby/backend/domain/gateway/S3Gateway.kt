package com.dobby.backend.domain.gateway

import com.dobby.backend.domain.dto.PreSignedUrlResponse

interface S3Gateway {
    fun getPreSignedUrl(fileName: String): PreSignedUrlResponse
}
