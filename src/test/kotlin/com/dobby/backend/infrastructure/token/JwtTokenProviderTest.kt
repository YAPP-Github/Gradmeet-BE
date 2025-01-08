package com.dobby.backend.infrastructure.token

import com.dobby.backend.domain.exception.AuthenticationTokenNotValidException
import com.dobby.backend.domain.model.Member
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertFailsWith

@SpringBootTest
@ActiveProfiles("test")
class JwtTokenProviderTest : BehaviorSpec() {

    @Autowired
    lateinit var jwtTokenProvider: JwtTokenProvider

    init {
        given("회원 정보가 주어지고") {
            val member = Member(id = 1, oauthEmail = "dlawotn3@naver.com", contactEmail = "dlawotn3@naver.com",
                provider = ProviderType.NAVER, role = RoleType.PARTICIPANT, name = "dobby",
                status = MemberStatus.ACTIVE)
            val authorities = listOf(SimpleGrantedAuthority(member.role?.name ?: "PARTICIPANT"))
            val authentication = UsernamePasswordAuthenticationToken(member.id, null, authorities)

            `when`("해당 인증 정보로 JWT 토큰을 생성하면") {
                val jwtToken = jwtTokenProvider.generateAccessToken(authentication)

                then("JWT 토큰이 생성된다") {
                    jwtToken shouldNotBe null
                }
            }
        }

        given("유효한 JWT 토큰이 주어지고") {
            val member = Member(id = 1, oauthEmail = "dlawotn3@naver.com", contactEmail = "dlawotn3@naver.com",
                provider = ProviderType.NAVER, role = RoleType.PARTICIPANT, name = "dobby",
                status = MemberStatus.ACTIVE)
            val authorities = listOf(SimpleGrantedAuthority(member.role?.name ?: "PARTICIPANT"))
            val authentication = UsernamePasswordAuthenticationToken(member.id, null, authorities)
            val validToken = jwtTokenProvider.generateAccessToken(authentication)

            `when`("해당 토큰을 파싱하면") {
                val parsedAuthentication = jwtTokenProvider.parseAuthentication(validToken)
                val extractedMemberId = parsedAuthentication.principal
                val extractedAuthorities = parsedAuthentication.authorities

                then("파싱된 멤버의 ID는 원래 멤버의 ID와 같아야 한다") {
                    extractedMemberId shouldNotBe null
                    extractedMemberId shouldBe member.id.toString()
                }

                then("파싱된 권한(role)은 원래 멤버의 역할과 같아야 한다") {
                    extractedAuthorities.size shouldBe 1
                    extractedAuthorities.first().authority shouldBe member.role?.name
                }
            }
        }

        given("유효하지 않은 JWT 토큰이 주어지고") {
            val invalidToken = "invalid.token.value"

            `when`("해당 토큰을 파싱하면") {
                val executor = {
                    jwtTokenProvider.parseAuthentication(invalidToken)
                }

                then("AuthenticationTokenNotValidException 예외가 발생한다") {
                    assertFailsWith<AuthenticationTokenNotValidException> {
                        executor()
                    }
                }
            }
        }
    }
}
