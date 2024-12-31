package com.dobby.backend.infrastructure.s3

import com.amazonaws.AmazonServiceException
import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.Headers
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.dobby.backend.domain.exception.InvalidInputException
import com.dobby.backend.presentation.api.dto.response.PreSignedUrlResponse
import com.dobby.backend.domain.gateway.S3Gateway
import com.dobby.backend.util.generateULID
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Slf4j
@Component
class S3PreSignedUrlProvider(
    private val amazonS3Client: AmazonS3,
) : S3Gateway {

    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    override fun getPreSignedUrl(fileName: String): PreSignedUrlResponse {
        val generatePreSignedUrlRequest = getGenerateImagePreSignedUrlRequest(fileName)
        return PreSignedUrlResponse(generatePreSignedUrl(generatePreSignedUrlRequest))
    }

    private fun generatePreSignedUrl(generatePresignedUrlRequest: GeneratePresignedUrlRequest): String {
        return try {
            amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString()
        } catch (e: AmazonServiceException) {
            throw IllegalStateException("Pre-signed Url 생성 실패했습니다.")
        }
    }

    private fun getGenerateImagePreSignedUrlRequest(fileName: String): GeneratePresignedUrlRequest {
        val savedImageName = generateUniqueImageName(fileName)
        val savedImagePath = "images/$savedImageName"
        return getPreSignedUrlRequest(bucket, savedImagePath)
    }

    private fun getPreSignedUrlRequest(bucket: String, fileName: String): GeneratePresignedUrlRequest {
        return GeneratePresignedUrlRequest(bucket, fileName)
            .withMethod(HttpMethod.PUT)
            .withExpiration(Date(System.currentTimeMillis() + 180000))
            .apply {
                addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString())
            }
    }

    private fun generateUniqueImageName(fileName: String): String {
        val lastDotIndex = fileName.lastIndexOf(".")
        if (lastDotIndex == -1 || lastDotIndex == fileName.length - 1) {
            throw InvalidInputException()
        }
        val ext = fileName.substring(lastDotIndex)
        return "${generateULID()}$ext"
    }
}
