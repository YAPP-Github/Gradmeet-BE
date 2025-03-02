package com.dobby.backend.infrastructure.s3

import com.dobby.domain.exception.InvalidRequestValueException
import com.dobby.domain.IdGenerator
import com.dobby.backend.infrastructure.config.properties.S3Properties
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest
import java.net.URL
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@SpringBootTest
@ActiveProfiles("test")
class S3PreSignedUrlProviderTest : BehaviorSpec({

    val s3Presigner = mockk<S3Presigner>()
    val s3Properties = mockk<S3Properties>()
    val s3ConfigProperties = mockk<S3Properties.S3>()
    val idGenerator = mockk<IdGenerator>()

    every { s3Properties.s3 } returns s3ConfigProperties
    every { s3ConfigProperties.bucket } returns "test-bucket"

    val provider = S3PreSignedUrlProvider(s3Presigner, idGenerator, s3Properties)

    given("이미지 파일 이름이 주어지고") {
        val imageName = "test_image.jpg"
        val mockUrl = URL("http://example.com/presigned")
        val mockPresignedRequest = mockk<PresignedPutObjectRequest>()

        every { s3Presigner.presignPutObject(any<PutObjectPresignRequest>()) } returns mockPresignedRequest
        every { mockPresignedRequest.url() } returns mockUrl
        every { idGenerator.generateId() } returns "test-tsid"

        `when`("S3에서 PreSigned URL을 생성하면") {
            then("PreSigned URL이 반환된다") {
                val preSignedUrl = provider.getExperimentPostPreSignedUrl(imageName)
                assertNotNull(preSignedUrl)
                assertEquals(mockUrl.toString(), preSignedUrl)
            }
        }
    }

    given("파일 이름에 확장자가 없는 경우") {
        val imageName = "test_image" // 확장자 없음

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
