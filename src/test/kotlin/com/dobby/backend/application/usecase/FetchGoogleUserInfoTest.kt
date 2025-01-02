package com.dobby.backend.application.usecase

import com.dobby.backend.domain.exception.OAuth2ProviderMissingException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@ActiveProfiles("test")
class FetchGoogleUserInfoUseCaseTest : BehaviorSpec({

    val webClientBuilder: WebClient.Builder = mockk(relaxed = true)
    val webClient: WebClient = mockk(relaxed = true)
    val requestHeadersUriSpec: WebClient.RequestHeadersUriSpec<*> = mockk(relaxed = true)
    val requestHeadersSpec: WebClient.RequestHeadersSpec<*> = mockk(relaxed = true)
    val responseSpec: WebClient.ResponseSpec = mockk(relaxed = true)
    val useCase = FetchGoogleUserInfoUseCase(webClientBuilder)

    beforeTest {
        every { webClientBuilder.build() } returns webClient
        every { webClient.get() } returns requestHeadersUriSpec
        every { requestHeadersUriSpec.uri(any<String>()) } returns requestHeadersSpec
        every { requestHeadersSpec.header(any(), any()) } returns requestHeadersSpec
        every { requestHeadersSpec.retrieve() } returns responseSpec
        every { responseSpec.bodyToMono(Map::class.java) } answers {
            Mono.just(mapOf("email" to "mock@example.com", "name" to "Mock User"))
        }
    }



    given("유효한 액세스 토큰이 주어졌을 때") {
        val validAccessToken = "validAccessToken"
        val userInfo = mapOf("email" to "test@example.com", "name" to "Test User")

        `when`("Google API에서 사용자 정보를 성공적으로 가져오면") {
            every { responseSpec.bodyToMono<Map<String, Any>>() } answers {
                println("Mock: bodyToMono 호출")
                Mono.just(userInfo)
            }
            val result = useCase.execute(validAccessToken)

            then("사용자 정보가 반환된다") {
                println("결과: $result")
                result["email"] shouldBe "test@example.com"
                result["name"] shouldBe "Test User"
            }

        }
    }

    given("유효하지 않은 액세스 토큰이 주어졌을 때") {
        val invalidAccessToken = "invalidAccessToken"

        `when`("Google API 호출 시 Unauthorized 예외가 발생하면") {
            every { responseSpec.bodyToMono<Map<String, Any>>() } answers {
                throw WebClientResponseException.Unauthorized.create(
                    401,
                    "Unauthorized",
                    HttpHeaders.EMPTY,
                    ByteArray(0),
                    null
                )
            }

            then("OAuth2ProviderMissingException 예외가 발생한다") {
                shouldThrow<OAuth2ProviderMissingException> {
                    useCase.execute(invalidAccessToken)
                }
            }
        }
    }

    given("Google API 응답이 비어 있을 때") {
        val emptyResponseToken = "emptyResponseToken"

        `when`("API 응답이 null인 경우") {
            every { responseSpec.bodyToMono<Map<String, Any>>() } returns Mono.empty()

            then("OAuth2ProviderMissingException 예외가 발생한다") {
                shouldThrow<OAuth2ProviderMissingException> {
                    useCase.execute(emptyResponseToken)
                }
            }
        }
    }
})