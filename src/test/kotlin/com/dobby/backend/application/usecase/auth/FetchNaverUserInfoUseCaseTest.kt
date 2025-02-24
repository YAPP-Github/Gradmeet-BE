package com.dobby.backend.application.usecase.auth

import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.gateway.auth.NaverAuthGateway
import com.dobby.backend.domain.gateway.auth.TokenGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.enums.member.MemberStatus
import com.dobby.backend.domain.enums.member.ProviderType
import com.dobby.backend.domain.enums.member.RoleType
import com.dobby.backend.presentation.api.dto.response.auth.naver.NaverTokenResponse
import com.dobby.backend.presentation.api.dto.response.auth.naver.NaverUserResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@ActiveProfiles("test")
class FetchNaverUserInfoUseCaseTest : BehaviorSpec({
    val naverAuthGateway = mockk<NaverAuthGateway>()
    val memberGateway = mockk<MemberGateway>()
    val tokenGateway = mockk<TokenGateway>()

    val fetchNaverUserInfoUseCase = FetchNaverUserInfoUseCase(
        naverAuthGateway,
        memberGateway,
        tokenGateway
    )

    given("Naver OAuth 요청이 들어왔을 때") {
        val input = FetchNaverUserInfoUseCase.Input(authorizationCode = "valid-auth-code", state = "valid-state", role = RoleType.PARTICIPANT)
        val mockMember = Member(
            id = "1",
            oauthEmail = "test@example.com",
            name = "Test User",
            status = MemberStatus.ACTIVE,
            role = RoleType.PARTICIPANT,
            contactEmail = "contact@example.com",
            provider = ProviderType.NAVER,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            deletedAt = null
        )

        val mockEmptyMember = null
        val mockNaverTokenResponse = NaverTokenResponse("mock-access-token")
        every { naverAuthGateway.getAccessToken(any(), any()) } returns mockNaverTokenResponse
        every { naverAuthGateway.getUserInfo("mock-access-token") } returns mockk {
            every { response } returns NaverUserResponse(
                email = "test@example.com"
            )
        }

        every { tokenGateway.generateAccessToken(any()) } returns "mock-jwt-access-token"
        every { tokenGateway.generateRefreshToken(any()) } returns "mock-jwt-refresh-token"

        // 테스트 1: 등록된 멤버가 있을 경우
        every { memberGateway.findByOauthEmailAndStatus("test@example.com", MemberStatus.ACTIVE) } returns mockMember

        `when`("정상적으로 등록된 유저가 있는 경우") {
            val result: FetchNaverUserInfoUseCase.Output = fetchNaverUserInfoUseCase.execute(input)

            then("유저 정보를 포함한 OauthLoginResponse를 반환해야 한다") {
                result.isRegistered shouldBe true
                result.accessToken shouldBe "mock-jwt-access-token"
                result.refreshToken shouldBe "mock-jwt-refresh-token"
                result.memberId shouldBe "1"
                result.oauthEmail shouldBe "test@example.com"
            }
        }

        // 테스트 2: 등록된 멤버가 없는 경우
        every { memberGateway.findByOauthEmailAndStatus("test@example.com", MemberStatus.ACTIVE) } returns mockEmptyMember

        `when`("등록되지 않은 유저가 있는 경우") {
            val result: FetchNaverUserInfoUseCase.Output = fetchNaverUserInfoUseCase.execute(input)

            then("isRegistered는 false, memberId는 null이어야 한다") {
                result.isRegistered shouldBe false
                result.memberId shouldBe null
                result.oauthEmail shouldBe "test@example.com"
            }
        }
    }
})
