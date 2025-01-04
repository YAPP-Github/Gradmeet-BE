package com.dobby.backend.util

import com.dobby.backend.infrastructure.database.entity.MemberEntity
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication

class AuthenticationUtilsTest : BehaviorSpec({
    given("유효한 MemberEntity가 주어졌을 때") {
        val member = MemberEntity(
            id = 1L,
            oauthEmail = "test@example.com",
            contactEmail = "contact@example.com",
            provider = ProviderType.GOOGLE,
            name = "Test User",
            role = RoleType.RESEARCHER
        )

        `when`("AuthenticationUtils.createAuthentication이 호출되면") {
            val authentication: Authentication = AuthenticationUtils.createAuthentication(member)

            then("UsernamePasswordAuthenticationToken 객체가 반환되어야 한다") {
                authentication shouldBe UsernamePasswordAuthenticationToken(
                    member, // principal
                    null, // credential
                    emptyList() // granted authorized sizes
                )
            }

            then("Authentication의 필드가 올바르게 설정되어야 한다") {
                authentication.principal shouldBe member
                authentication.credentials shouldBe null
                authentication.authorities.size shouldBe 0
            }
        }
    }
})
