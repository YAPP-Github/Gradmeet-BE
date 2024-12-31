package com.dobby.backend.domain.gateway

import com.dobby.backend.presentation.api.dto.response.PreSignedUrlResponse

interface S3Gateway {
    fun getPreSignedUrl(fileName: String): PreSignedUrlResponse
}
