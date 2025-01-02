package com.dobby.backend.application.mapper

import com.dobby.backend.domain.exception.OAuth2EmailNotFoundException
import com.dobby.backend.domain.exception.OAuth2NameNotFoundException
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.presentation.api.dto.request.OauthUserDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.oauth2.core.user.OAuth2User

class OauthUserMapperTest : BehaviorSpec({
    given("유효한 OAuth2User 객체가 주어졌을 때") {
        `when`("toDto를 호출하면") {
            val oauthUser = mockk<OAuth2User>()
            every { oauthUser.getAttribute<String>("email") } returns "test@example.com"
            every { oauthUser.getAttribute<String>("name") } returns "Test User"

            val result = OauthUserMapper.toDto(oauthUser, ProviderType.GOOGLE)

            then("올바르게 OauthUserDto로 매핑된다") {
                result.email shouldBe "test@example.com"
                result.name shouldBe "Test User"
                result.provider shouldBe ProviderType.GOOGLE
            }
        }
    }

    given("email 속성이 없는 OAuth2User 객체가 주어졌을 때") {
        `when`("toDto를 호출하면") {
            val oauthUser = mockk<OAuth2User>()
            every { oauthUser.getAttribute<String>("email") } returns null
            every { oauthUser.getAttribute<String>("name") } returns "Test User"

            then("OAuth2EmailNotFoundException 예외가 발생한다") {
                shouldThrow<OAuth2EmailNotFoundException> {
                    OauthUserMapper.toDto(oauthUser, ProviderType.GOOGLE)
                }
            }
        }
    }

    given("name 속성이 없는 OAuth2User 객체가 주어졌을 때") {
        `when`("toDto를 호출하면") {
            val oauthUser = mockk<OAuth2User>()
            every { oauthUser.getAttribute<String>("email") } returns "test@example.com"
            every { oauthUser.getAttribute<String>("name") } returns null

            then("OAuth2NameNotFoundException 예외가 발생한다") {
                shouldThrow<OAuth2NameNotFoundException> {
                    OauthUserMapper.toDto(oauthUser, ProviderType.GOOGLE)
                }
            }
        }
    }

    given("유효한 OauthUserDto 객체가 주어졌을 때") {
        `when`("toTempMember를 호출하면") {
            val dto = OauthUserDto(
                email = "test@example.com",
                name = "Test User",
                provider = ProviderType.GOOGLE
            )
            val result = OauthUserMapper.toTempMember(dto)

            then("올바르게 Member 객체로 매핑된다") {
                result.oauthEmail shouldBe "test@example.com"
                result.name shouldBe "Test User"
                result.provider shouldBe ProviderType.GOOGLE
                result.status shouldBe com.dobby.backend.infrastructure.database.entity.enum.MemberStatus.HOLD
            }
        }
    }
})
