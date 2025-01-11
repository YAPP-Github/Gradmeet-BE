package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.mapper.SignupMapper
import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.gateway.MemberGateway
import com.dobby.backend.domain.gateway.ResearcherGateway
import com.dobby.backend.domain.gateway.TokenGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.model.member.Researcher
import com.dobby.backend.infrastructure.database.entity.enum.*

class CreateResearcherUseCase(
    private val memberGateway: MemberGateway,
    private val researcherGateway: ResearcherGateway,
    private val tokenGateway: TokenGateway,
) : UseCase<CreateResearcherUseCase.Input, CreateResearcherUseCase.Output> {
    data class Input(
        val oauthEmail: String,
        val provider: ProviderType,
        val contactEmail: String,
        val univEmail : String,
        val emailVerified: Boolean,
        val univName: String,
        val name : String,
        val major: String,
        val labInfo : String?
    )

    data class Output(
        val accessToken: String,
        val refreshToken: String,
        val memberInfo: MemberResponse
    )
    data class MemberResponse(
        val memberId: Long?,
        val name: String?,
        val oauthEmail: String?,
        val provider: ProviderType?,
        val role: RoleType?,
    )

    override fun execute(input: Input): Output {
        val savedResearcher = createResearcher(input)
        val savedMember = savedResearcher.member
        savedMember.status = MemberStatus.ACTIVE
        val updatedMember = memberGateway.save(savedMember)

        val accessToken = tokenGateway.generateAccessToken(updatedMember)
        val refreshToken = tokenGateway.generateRefreshToken(updatedMember)

        return Output(
            accessToken = accessToken,
            refreshToken = refreshToken,
            memberInfo = SignupMapper.modelToResearcherRes(savedResearcher)
        )
    }

    private fun createResearcher(input: Input): Researcher {
        val member = Member(
            id = 0L,
            status = MemberStatus.ACTIVE,
            oauthEmail = input.oauthEmail,
            contactEmail = input.contactEmail,
            provider = input.provider,
            role = RoleType.RESEARCHER,
            name = input.name
        )

        val researcher = Researcher(
            id = 0L,
            member = member,
            univEmail = input.univEmail,
            univName = input.univName,
            emailVerified = input.emailVerified,
            major = input.major,
            labInfo = input.labInfo
        )
        val newResearcher = researcherGateway.save(researcher)
        return newResearcher
    }

}
