package com.dobby.backend.infrastructure.token

import com.dobby.backend.domain.exception.AuthenticationTokenNotValidException
import com.dobby.backend.domain.model.Member
import io.kotest.core.spec.style.BehaviorSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@SpringBootTest
class JwtTokenProviderTest : BehaviorSpec() {

    @Autowired
    lateinit var jwtTokenProvider: JwtTokenProvider

    init {
        given("회원 정보가 주어지고") {
            val member = Member(memberId = 1, email = "dlawotn3@naver.com", name = "dobby")
            val authentication = UsernamePasswordAuthenticationToken(member.memberId, null)

            `when`("해당 인증 정보로 JWT 토큰을 생성하면") {
                val jwtToken = jwtTokenProvider.generateAccessToken(authentication)

                then("JWT 토큰이 생성된다") {
                    assertNotNull(jwtToken)
                }
            }
        }

        given("유효한 JWT 토큰이 주어지고") {
            val member = Member(memberId = 1, email = "dlawotn3@naver.com", name = "dobby")
            val authentication = UsernamePasswordAuthenticationToken(member.memberId, null)
            val validToken = jwtTokenProvider.generateAccessToken(authentication)

            `when`("해당 토큰을 파싱하면") {
                val parsedAuthentication = jwtTokenProvider.parseAuthentication(validToken)
                val extractedMemberId = parsedAuthentication.principal

                then("파싱된 멤버의 ID는 원래 멤버의 ID와 같아야 한다") {
                    assertNotNull(extractedMemberId)
                    assertEquals(member.memberId.toString(), extractedMemberId)
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
