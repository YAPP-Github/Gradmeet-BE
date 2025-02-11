package com.dobby.backend.infrastructure.s3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.dobby.backend.domain.exception.InvalidRequestValueException
import com.dobby.backend.domain.IdGenerator
import com.dobby.backend.infrastructure.config.properties.S3Properties
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.net.URL
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@SpringBootTest
@ActiveProfiles("test")
class S3PreSignedUrlProviderTest : BehaviorSpec({

    val amazonS3Client = mockk<AmazonS3>()
    val s3Properties = mockk<S3Properties>()
    val s3Config = mockk<S3Properties.S3>()
    val idGenerator = mockk<IdGenerator>()

    every { s3Properties.s3 } returns s3Config
    every { s3Config.bucket } returns "test-bucket"

    val provider = S3PreSignedUrlProvider(amazonS3Client, idGenerator, s3Properties)

    given("이미지 파일 이름이 주어지고") {
        val imageName = "test_image.jpg"
        val mockPresignedUrl = mockk<URL>()

        `when`("S3에서 PreSigned URL을 생성하면") {
            every { amazonS3Client.generatePresignedUrl(any<GeneratePresignedUrlRequest>()) } returns mockPresignedUrl
            every { idGenerator.generateId() } returns "test-tsid"

            then("PreSigned URL이 반환된다") {
                val preSignedUrl = provider.getExperimentPostPreSignedUrl(imageName)
                assertNotNull(preSignedUrl)
                assertEquals(mockPresignedUrl.toString(), preSignedUrl)
            }
        }
    }

    given("파일 이름에 확장자가 없는 경우") {
        val imageName = "test_image" // 확장자가 없는 파일명

        then("InvalidRequestValueException이 발생한다") {
            assertFailsWith<InvalidRequestValueException> {
                provider.getExperimentPostPreSignedUrl(imageName)
            }
        }
    }

    given("generateId 함수가 호출되면") {
        every { idGenerator.generateId() } returns "test-tsid"
        val tsid = idGenerator.generateId()

        then("TSID가 생성된다") {
            assertNotNull(tsid)
            assertEquals("test-tsid", tsid)
        }
    }
})
