import com.dobby.backend.application.service.OauthService
import com.dobby.backend.application.usecase.FetchGoogleUserInfoUseCase
import com.dobby.backend.application.usecase.FetchNaverUserInfoUseCase
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.presentation.api.dto.request.auth.google.GoogleOauthLoginRequest
import com.dobby.backend.presentation.api.dto.response.MemberResponse
import com.dobby.backend.presentation.api.dto.response.auth.OauthLoginResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
class OauthServiceTest : BehaviorSpec({
    val fetchGoogleUserInfoUseCase = mockk<FetchGoogleUserInfoUseCase>()
    val fetchNaverUserInfoUseCase = mockk<FetchNaverUserInfoUseCase>()

    val oauthService = OauthService(fetchGoogleUserInfoUseCase, fetchNaverUserInfoUseCase)

    given("Google OAuth 요청이 들어왔을 때") {
        val oauthLoginRequest = GoogleOauthLoginRequest(authorizationCode = "valid-auth-code")
        val expectedResponse = OauthLoginResponse(
            isRegistered = true,
            accessToken = "mock-access-token",
            refreshToken = "mock-refresh-token",
            memberInfo = MemberResponse(
                memberId = 1L,
                oauthEmail = "test@example.com",
                name = "Test User",
                role = RoleType.PARTICIPANT,
                provider = ProviderType.GOOGLE
            )
        )

        `when`("FetchGoogleUserInfoUseCase가 올바른 응답을 반환하면") {
            every { fetchGoogleUserInfoUseCase.execute(oauthLoginRequest) } returns expectedResponse

            then("OauthService는 동일한 응답을 반환해야 한다") {
                val result = oauthService.getGoogleUserInfo(oauthLoginRequest)
                result shouldBe expectedResponse
            }
        }
    }
})
