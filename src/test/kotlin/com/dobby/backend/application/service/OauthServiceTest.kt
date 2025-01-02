package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.FetchGoogleUserInfoUseCase
import com.dobby.backend.domain.exception.OAuth2AuthenticationException
import com.dobby.backend.domain.exception.OAuth2EmailNotFoundException
import com.dobby.backend.domain.exception.OAuth2NameNotFoundException
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.presentation.api.dto.response.OauthTokenResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class OauthServiceTest : BehaviorSpec({

    // Mocking the UseCase
    val fetchGoogleUserInfoUseCase: FetchGoogleUserInfoUseCase = mockk()
    val oauthService = OauthService(fetchGoogleUserInfoUseCase)

    given("유효한 액세스 토큰이 주어졌을 때") {
        val accessToken = "validAccessToken"
        val userInfo = mapOf("email" to "gradmeet@test.com", "name" to "Test User")

        `when`("OAuth Service에서 사용자 정보를 가져오면") {
            every { fetchGoogleUserInfoUseCase.execute(accessToken) } returns userInfo

            val response = oauthService.getGoogleUserInfo(accessToken)

            then("사용자 정보가 정상적으로 반환된다") {
                response.email shouldBe "gradmeet@test.com"
                response.name shouldBe "Test User"
                response.provider shouldBe ProviderType.GOOGLE
            }
        }
    }

    given("유효하지 않은 액세스 토큰이 주어졌을 때") {
        val invalidToken = "invalidAccessToken"

        `when`("OAuth 서비스에서 사용자 정보를 가져오려 하면") {
            every { fetchGoogleUserInfoUseCase.execute(invalidToken) } throws OAuth2AuthenticationException()

            then("OAuth2AuthenticationException 예외가 발생한다") {
                shouldThrow<OAuth2AuthenticationException> {
                    oauthService.getGoogleUserInfo(invalidToken)
                }
            }
        }
    }

    given("액세스 토큰이 이메일 없이 주어졌을 때") {
        val accessToken = "tokenWithoutEmail"
        val userInfo = mapOf("name" to "Test User")

        `when`("OAuth 서비스에서 사용자 정보를 가져오려 하면") {
            every { fetchGoogleUserInfoUseCase.execute(accessToken) } returns userInfo

            then("OAuth2EmailNotFoundException 예외가 발생한다") {
                shouldThrow<OAuth2EmailNotFoundException> {
                    oauthService.getGoogleUserInfo(accessToken)
                }
            }
        }
    }

    given("액세스 토큰이 이름 없이 주어졌을 때") {
        val accessToken = "tokenWithoutName"
        val userInfo = mapOf("email" to "gradmeet@test.com")

        `when`("OAuth 서비스에서 사용자 정보를 가져오려 하면") {
            every { fetchGoogleUserInfoUseCase.execute(accessToken) } returns userInfo

            then("OAuth2NameNotFoundException 예외가 발생한다") {
                shouldThrow<OAuth2NameNotFoundException> {
                    oauthService.getGoogleUserInfo(accessToken)
                }
            }
        }
    }
})

