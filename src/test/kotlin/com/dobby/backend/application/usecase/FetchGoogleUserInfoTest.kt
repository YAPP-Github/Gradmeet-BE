import com.dobby.backend.application.usecase.FetchGoogleUserInfoUseCase
import com.dobby.backend.infrastructure.config.properties.GoogleAuthProperties
import com.dobby.backend.infrastructure.database.entity.MemberEntity
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.infrastructure.database.repository.MemberRepository
import com.dobby.backend.infrastructure.feign.google.GoogleAuthFeignClient
import com.dobby.backend.infrastructure.feign.google.GoogleUserInfoFeginClient
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.presentation.api.dto.request.auth.GoogleOauthLoginRequest
import com.dobby.backend.presentation.api.dto.response.auth.GoogleTokenResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

@ActiveProfiles("test")
class FetchGoogleUserInfoUseCaseTest : BehaviorSpec({
    val googleAuthFeignClient = mockk<GoogleAuthFeignClient>()
    val googleUserInfoFeginClient = mockk<GoogleUserInfoFeginClient>()
    val jwtTokenProvider = mockk<JwtTokenProvider>()
    val googleAuthProperties = mockk<GoogleAuthProperties>()
    val memberRepository = mockk<MemberRepository>()

    val fetchGoogleUserInfoUseCase = FetchGoogleUserInfoUseCase(
        googleAuthFeignClient,
        googleUserInfoFeginClient,
        jwtTokenProvider,
        googleAuthProperties,
        memberRepository
    )

    given("Google OAuth 요청이 들어왔을 때") {
        val oauthLoginRequest = GoogleOauthLoginRequest(authorizationCode = "valid-auth-code")
        val mockMember = MemberEntity(
            id = 1L,
            oauthEmail = "test@example.com",
            name = "Test User",
            status = MemberStatus.ACTIVE,
            role = RoleType.PARTICIPANT,
            birthDate = LocalDate.of(2002, 11, 21),
            contactEmail = "contact@example.com",
            provider = ProviderType.GOOGLE
        )

        every { googleAuthProperties.clientId } returns "mock-client-id"
        every { googleAuthProperties.clientSecret } returns "mock-client-secret"
        every { googleAuthProperties.redirectUri } returns "http://localhost/callback"
        every { googleAuthFeignClient.getAccessToken(any()) } returns GoogleTokenResponse("mock-access-token")
        every { googleUserInfoFeginClient.getUserInfo(any()) } returns mockk {
            every { email } returns "test@example.com"
            every { name } returns "Test User"
        }
        every { memberRepository.findByOauthEmailAndStatus("test@example.com", MemberStatus.ACTIVE) } returns mockMember
        every { jwtTokenProvider.generateAccessToken(any()) } returns "mock-jwt-access-token"
        every { jwtTokenProvider.generateRefreshToken(any()) } returns "mock-jwt-refresh-token"

        `when`("정상적으로 모든 데이터가 주어지면") {
            val result = fetchGoogleUserInfoUseCase.execute(oauthLoginRequest)

            then("유저 정보를 포함한 OauthLoginResponse를 반환해야 한다") {
                result.isRegistered shouldBe true
                result.accessToken shouldBe "mock-jwt-access-token"
                result.refreshToken shouldBe "mock-jwt-refresh-token"
                result.memberInfo.oauthEmail shouldBe "test@example.com"
                result.memberInfo.name shouldBe "Test User"
            }
        }
    }
})