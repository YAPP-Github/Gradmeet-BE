package com.dobby.backend.application.usecase

import com.dobby.backend.domain.exception.SignInMemberException
import com.dobby.backend.domain.gateway.MemberGateway
import com.dobby.backend.domain.gateway.NaverAuthGateway
import com.dobby.backend.domain.gateway.TokenGateway
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.infrastructure.database.repository.MemberRepository
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.util.AuthenticationUtils
import java.lang.Exception

class FetchNaverUserInfoUseCase(
    private val naverAuthGateway: NaverAuthGateway,
    private val memberGateway: MemberGateway,
    private val jwtTokenGateway: TokenGateway
) : UseCase<FetchNaverUserInfoUseCase.Input, FetchNaverUserInfoUseCase.Output> {

    data class Input(
        val authorizationCode: String,
        val state: String
    )

    data class Output(
        val isRegistered: Boolean,
        val accessToken: String,
        val refreshToken: String,
        val memberId: Long?,
        val oauthEmail: String,
        val oauthName: String,
        val role: RoleType?,
        val provider: ProviderType
    )

    override fun execute(input: Input): Output {
        try {
            val oauthToken = naverAuthGateway.getAccessToken(input.authorizationCode, input.state)
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
                    oauthName = member.name ?: throw SignInMemberException(),
                    role = member.role ?: throw SignInMemberException(),
                    provider = ProviderType.NAVER
                )
            } else {
                // 등록된 멤버가 없으면 isRegistered = false, memberId = null
                Output(
                    isRegistered = false,
                    accessToken = "",
                    refreshToken = "",
                    memberId = null,
                    oauthEmail = email ?: "",
                    oauthName = "",
                    role = null,
                    provider = ProviderType.NAVER
                )
            }
        } catch (e: Exception) {
            throw SignInMemberException()
        }
    }
}
