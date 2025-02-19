package com.dobby.backend.infrastructure.s3

import com.dobby.backend.domain.exception.InvalidRequestValueException
import com.dobby.backend.domain.IdGenerator
import com.dobby.backend.infrastructure.config.properties.S3Properties
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration

@Component
class S3PreSignedUrlProvider(
    private val s3Presigner: S3Presigner,
    private val idGenerator: IdGenerator,
    properties: S3Properties
) {
    private val bucket: String = properties.s3.bucket

    fun getExperimentPostPreSignedUrl(fileName: String): String {
        val generatePreSignedUrlRequest = getGenerateImagePreSignedUrlRequest("experiment-post", fileName)
        return generatePreSignedUrl(generatePreSignedUrlRequest)
    }

    private fun generatePreSignedUrl(putObjectPresignRequest: PutObjectPresignRequest): String {
        return try {
            s3Presigner.presignPutObject(putObjectPresignRequest).url().toString()
        } catch (e: Exception) {
            throw IllegalStateException("Pre-signed Url 생성 실패했습니다.")
        }
    }

    private fun getGenerateImagePreSignedUrlRequest(directory: String, fileName: String): PutObjectPresignRequest {
        val savedImageName = generateUniqueImageName(fileName)
        val savedImagePath = "images/$directory/$savedImageName"
        return getPreSignedUrlRequest(bucket, savedImagePath)
    }

    private fun getPreSignedUrlRequest(bucket: String, key: String): PutObjectPresignRequest {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()

        return PutObjectPresignRequest.builder()
            .putObjectRequest(putObjectRequest)
            .signatureDuration(Duration.ofSeconds(300))
            .build()
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
