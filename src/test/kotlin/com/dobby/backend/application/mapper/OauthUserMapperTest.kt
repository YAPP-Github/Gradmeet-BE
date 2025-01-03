package com.dobby.backend.application.mapper

import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
class OauthUserMapperTest : BehaviorSpec({
<<<<<<< HEAD
     given("OauthUserMapper가 호출될 때") {
         val isRegistered = true
         val accessTgit oken = "mock-access-token"
         val refreshToken = "mock-refresh-token"
         val oauthEmail = "test@example.com"
         val oauthName = "Test User"
         val role = RoleType.PARTICIPANT
         val provider = ProviderType.GOOGLE
         val memberId = 1L
=======
    given("OauthUserMapper가 호출될 때") {
        val isRegistered = true
        val accessToken = "mock-access-token"
        val refreshToken = "mock-refresh-token"
        val oauthEmail = "test@example.com"
        val oauthName = "Test User"
        val role = RoleType.PARTICIPANT
        val provider = ProviderType.GOOGLE
        val memberId = 1L
>>>>>>> 4930f33 (refact: refactor OAuthLoginResponse response structure)

        `when`("toDto 메서드가 호출되면") {
            val result = OauthUserMapper.toDto(
                isRegistered = isRegistered,
                accessToken = accessToken,
                refreshToken = refreshToken,
                oauthEmail = oauthEmail,
                oauthName = oauthName,
                role = role,
                provider = provider,
                memberId = memberId
            )

            then("올바른 OauthLoginResponse가 반환되어야 한다") {
                result.isRegistered shouldBe isRegistered
                result.accessToken shouldBe accessToken
                result.refreshToken shouldBe refreshToken

                val memberInfo = result.memberInfo
                memberInfo.memberId shouldBe memberId
                memberInfo.oauthEmail shouldBe oauthEmail
                memberInfo.name shouldBe oauthName
                memberInfo.role shouldBe role
                memberInfo.provider shouldBe provider
            }
        }
    }
})
