package com.dobby.backend.application.usecase

import com.dobby.backend.domain.gateway.MemberGateway
import com.dobby.backend.domain.gateway.feign.NaverAuthGateway
import com.dobby.backend.domain.gateway.TokenGateway
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType

class FetchNaverUserInfoUseCase(
    private val naverAuthGateway: NaverAuthGateway,
    private val memberGateway: MemberGateway,
    private val jwtTokenGateway: TokenGateway
) : UseCase<FetchNaverUserInfoUseCase.Input, FetchNaverUserInfoUseCase.Output> {

    data class Input(
        val authorizationCode: String,
        val state: String
    )

    // TODO: 테스트 후, oauthEmail not null 처리
    data class Output(
        val isRegistered: Boolean,
        val accessToken: String?,
        val refreshToken: String?,
        val memberId: Long?,
        val oauthEmail: String?,
        val oauthName: String?,
        val role: RoleType?,
        val provider: ProviderType
    )

    override fun execute(input: Input): Output {
        val oauthToken = naverAuthGateway.getAccessToken(input.authorizationCode, input.state).accessToken
        val userInfo = oauthToken?.let { naverAuthGateway.getUserInfo(it) }
        val email = userInfo?.email
        val member = email?.let { memberGateway.findByOauthEmailAndStatus(it, MemberStatus.ACTIVE) }

        return if (member != null) {
            val jwtAccessToken = jwtTokenGateway.generateAccessToken(member)
            val jwtRefreshToken = jwtTokenGateway.generateRefreshToken(member)

            Output(
                isRegistered = true,
                accessToken = jwtAccessToken,
                refreshToken = jwtRefreshToken,
                memberId = member.id,
                oauthEmail = member.oauthEmail,
                oauthName = member.name,
                role = member.role,
                provider = ProviderType.NAVER
            )
        } else {
            // 등록된 멤버가 없으면 isRegistered = false, memberId = null
            Output(
                isRegistered = false,
                accessToken = null,
                refreshToken = null,
                memberId = null,
                oauthEmail = email,
                oauthName = null,
                role = null,
                provider = ProviderType.NAVER
            )
        }
    }
}
