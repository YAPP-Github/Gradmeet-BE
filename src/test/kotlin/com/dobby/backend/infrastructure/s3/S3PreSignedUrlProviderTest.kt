package com.dobby.backend.infrastructure.s3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.dobby.backend.domain.exception.InvalidInputException
import com.dobby.backend.infrastructure.config.properties.S3Properties
import com.dobby.backend.util.generateULID
import io.kotest.core.spec.style.BehaviorSpec
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import java.net.URL
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@SpringBootTest
class S3PreSignedUrlProviderTest : BehaviorSpec({

    val amazonS3Client = mock(AmazonS3::class.java)
    val s3Properties = mock(S3Properties::class.java)
    val s3Config = mock(S3Properties.S3::class.java)

    `when`(s3Properties.s3).thenReturn(s3Config)
    `when`(s3Config.bucket).thenReturn("test-bucket")
    `when`(s3Properties.s3.bucket).thenReturn("test-bucket")
    val provider = S3PreSignedUrlProvider(amazonS3Client, s3Properties)

    given("이미지 파일 이름이 주어지고") {
        val imageName = "test_image.jpg"
        val mockPresignedUrl = mock(URL::class.java)

        `when`("S3에서 PreSigned URL을 생성하면") {
            `when`(amazonS3Client.generatePresignedUrl(any(GeneratePresignedUrlRequest::class.java))).thenReturn(mockPresignedUrl)

            then("PreSigned URL이 반환된다") {
                val preSignedUrl = provider.getExperimentPostPreSignedUrl(imageName)
                assertNotNull(preSignedUrl)
                assertEquals(mockPresignedUrl.toString(), preSignedUrl)
            }
        }
    }

    given("파일 이름에 확장자가 없는 경우") {
        val imageName = "test_image" // 확장자가 없는 파일명

        then("InvalidInputException이 발생한다") {
            assertFailsWith<InvalidInputException> {
                provider.getExperimentPostPreSignedUrl(imageName)
            }
        }
    }

    given("generateULID 함수가 호출되면") {
        val ulid = generateULID()

        then("ULID가 생성된다") {
            assertNotNull(ulid)
            assertTrue(ulid.isNotEmpty())
        }
    }
})
