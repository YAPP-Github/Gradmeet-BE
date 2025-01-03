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
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import java.time.LocalDate
=======
import org.springframework.test.context.ActiveProfiles
<<<<<<< HEAD
import kotlin.test.assertEquals
>>>>>>> dc4d52e ([YS-31] feat: 구글 OAuth 로그인 구현 (#13))
=======
>>>>>>> a7fa0d8 (test: refactor JwtTokenProviderTest to use Kotest style)
=======
=======
>>>>>>> d02399a (fix: fix conflicts for merge)
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
<<<<<<< HEAD
>>>>>>> 6387bdb (test: fix test due to changed domain)
>>>>>>> e59675c (test: fix test due to changed domain)
=======
>>>>>>> d02399a (fix: fix conflicts for merge)
import kotlin.test.assertFailsWith

@SpringBootTest
@ActiveProfiles("test")
class JwtTokenProviderTest : BehaviorSpec() {

    @Autowired
    lateinit var jwtTokenProvider: JwtTokenProvider

    init {
        given("회원 정보가 주어지고") {
            val member = Member(memberId = 1, oauthEmail = "dlawotn3@naver.com", contactEmail = "dlawotn3@naver.com",
                provider = ProviderType.NAVER, role = RoleType.PARTICIPANT, name = "dobby",
                birthDate = LocalDate.of(2000, 7, 8), status = MemberStatus.ACTIVE)
            val authentication = UsernamePasswordAuthenticationToken(member.memberId, null)

            `when`("해당 인증 정보로 JWT 토큰을 생성하면") {
                val jwtToken = jwtTokenProvider.generateAccessToken(authentication)

                then("JWT 토큰이 생성된다") {
                    jwtToken shouldNotBe null
                }
            }
        }

        given("유효한 JWT 토큰이 주어지고") {
            val member = Member(memberId = 1, oauthEmail = "dlawotn3@naver.com", contactEmail = "dlawotn3@naver.com",
                provider = ProviderType.NAVER, role = RoleType.PARTICIPANT, name = "dobby",
                birthDate = LocalDate.of(2000, 7, 8), status = MemberStatus.ACTIVE)
            val authentication = UsernamePasswordAuthenticationToken(member.memberId, null)
            val validToken = jwtTokenProvider.generateAccessToken(authentication)

            `when`("해당 토큰을 파싱하면") {
                val parsedAuthentication = jwtTokenProvider.parseAuthentication(validToken)
                val extractedMemberId = parsedAuthentication.principal

                then("파싱된 멤버의 ID는 원래 멤버의 ID와 같아야 한다") {
                    extractedMemberId shouldNotBe null
                    extractedMemberId shouldBe member.memberId.toString()
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
