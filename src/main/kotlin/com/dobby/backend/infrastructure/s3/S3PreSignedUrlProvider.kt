package com.dobby.backend.infrastructure.s3

import com.amazonaws.AmazonServiceException
import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.Headers
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.dobby.backend.domain.exception.InvalidRequestValueException
import com.dobby.backend.domain.IdGenerator
import com.dobby.backend.infrastructure.config.properties.S3Properties
import org.springframework.stereotype.Component
import java.util.*

@Component
class S3PreSignedUrlProvider(
    private val amazonS3Client: AmazonS3,
    private val idGenerator: IdGenerator,
    properties: S3Properties
) {
    private val bucket: String = properties.s3.bucket

    fun getExperimentPostPreSignedUrl(fileName: String): String {
        val generatePreSignedUrlRequest = getGenerateImagePreSignedUrlRequest("experiment-post", fileName)
        return generatePreSignedUrl(generatePreSignedUrlRequest)
    }

    private fun generatePreSignedUrl(generatePresignedUrlRequest: GeneratePresignedUrlRequest): String {
        return try {
            amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString()
        } catch (e: AmazonServiceException) {
            throw IllegalStateException("Pre-signed Url 생성 실패했습니다.")
        }
    }

    private fun getGenerateImagePreSignedUrlRequest(directory: String, fileName: String): GeneratePresignedUrlRequest {
        val savedImageName = generateUniqueImageName(fileName)
        val savedImagePath = "images/$directory/$savedImageName"
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
            throw InvalidRequestValueException
        }
        val ext = fileName.substring(lastDotIndex)
        return "${idGenerator.generateId()}$ext"
    }
}
