package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.gateway.IdGeneratorGateway
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.gateway.member.ResearcherGateway
import com.dobby.backend.domain.gateway.auth.TokenGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.model.member.Researcher
import com.dobby.backend.infrastructure.database.entity.enums.*
import java.time.LocalDateTime

class CreateResearcherUseCase(
    private val memberGateway: MemberGateway,
    private val researcherGateway: ResearcherGateway,
    private val tokenGateway: TokenGateway,
    private val idGeneratorGateway: IdGeneratorGateway
) : UseCase<CreateResearcherUseCase.Input, CreateResearcherUseCase.Output> {
    data class Input(
        val oauthEmail: String,
        val provider: ProviderType,
        val contactEmail: String,
        val univEmail : String,
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
        val memberId: String?,
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
            memberInfo = MemberResponse(
                memberId = savedMember.id,
                name = savedMember.name,
                oauthEmail = savedMember.oauthEmail,
                provider = savedMember.provider,
                role = savedMember.role
            )
        )
    }

    private fun createResearcher(input: Input): Researcher {
        val member = Member.newMember(
            id = idGeneratorGateway.generateId(),
            oauthEmail = input.oauthEmail,
            contactEmail = input.contactEmail,
            provider = input.provider,
            role = RoleType.RESEARCHER,
            name = input.name,
        )

        val researcher = Researcher.newResearcher(
            id = idGeneratorGateway.generateId(),
            member = member,
            univEmail = input.univEmail,
            univName = input.univName,
            emailVerified = true,
            major = input.major,
            labInfo = input.labInfo
        )
        return researcherGateway.save(researcher)
    }

}
