import com.dobby.backend.application.usecase.FetchNaverUserInfoUseCase
import com.dobby.backend.infrastructure.config.properties.NaverAuthProperties
import com.dobby.backend.infrastructure.database.entity.MemberEntity
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.infrastructure.database.repository.MemberRepository
import com.dobby.backend.infrastructure.feign.naver.NaverAuthFeignClient
import com.dobby.backend.infrastructure.feign.naver.NaverUserInfoFeignClient
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.presentation.api.dto.request.auth.NaverOauthLoginRequest
import com.dobby.backend.presentation.api.dto.response.auth.OauthLoginResponse
import com.dobby.backend.presentation.api.dto.response.auth.NaverTokenResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

@ActiveProfiles("test")
class FetchNaverUserInfoUseCaseTest : BehaviorSpec({
    val naverAuthFeignClient = mockk<NaverAuthFeignClient>()
    val naverUserInfoFeginClient = mockk<NaverUserInfoFeignClient>()
    val jwtTokenProvider = mockk<JwtTokenProvider>()
    val naverAuthProperties = mockk<NaverAuthProperties>()
    val memberRepository = mockk<MemberRepository>()

    val fetchNaverUserInfoUseCase = FetchNaverUserInfoUseCase(
        naverAuthFeignClient,
        naverUserInfoFeginClient,
        jwtTokenProvider,
        naverAuthProperties,
        memberRepository
    )

    given("Naver OAuth 요청이 들어왔을 때") {
        val oauthLoginRequest = NaverOauthLoginRequest(authorizationCode = "valid-auth-code", state = "valid-state")
        val mockMember = MemberEntity(
            id = 1L,
            oauthEmail = "test@example.com",
            name = "Test User",
            status = MemberStatus.ACTIVE,
            role = RoleType.PARTICIPANT,
            birthDate = LocalDate.of(2002, 11, 21),
            contactEmail = "contact@example.com",
            provider = ProviderType.NAVER
        )

        every { naverAuthProperties.clientId } returns "mock-client-id"
        every { naverAuthProperties.clientSecret } returns "mock-client-secret"
        every { naverAuthProperties.redirectUri } returns "http://localhost/callback"
        every { naverAuthFeignClient.getAccessToken(any()) } returns NaverTokenResponse("mock-access-token")
        every { naverUserInfoFeginClient.getUserInfo("Bearer mock-access-token") } returns mockk {
            every { email } returns "test@example.com"
            every { name } returns "Test User"
        }
        every { memberRepository.findByOauthEmailAndStatus("test@example.com", MemberStatus.ACTIVE) } returns mockMember
        every { jwtTokenProvider.generateAccessToken(any()) } returns "mock-jwt-access-token"
        every { jwtTokenProvider.generateRefreshToken(any()) } returns "mock-jwt-refresh-token"

        `when`("정상적으로 모든 데이터가 주어지면") {
            val result: OauthLoginResponse = fetchNaverUserInfoUseCase.execute(oauthLoginRequest)

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
