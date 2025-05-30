package com.dobby.usecase.member

import com.dobby.enums.member.MemberStatus
import com.dobby.enums.member.ProviderType
import com.dobby.enums.member.RoleType
import com.dobby.gateway.auth.TokenGateway
import com.dobby.gateway.member.MemberConsentGateway
import com.dobby.gateway.member.MemberGateway
import com.dobby.gateway.member.ResearcherGateway
import com.dobby.model.member.Member
import com.dobby.model.member.MemberConsent
import com.dobby.model.member.Researcher
import com.dobby.usecase.UseCase
import com.dobby.util.IdGenerator

class CreateResearcherUseCase(
    private val memberGateway: MemberGateway,
    private val researcherGateway: ResearcherGateway,
    private val memberConsentGateway: MemberConsentGateway,
    private val tokenGateway: TokenGateway,
    private val idGenerator: IdGenerator
) : UseCase<CreateResearcherUseCase.Input, CreateResearcherUseCase.Output> {
    data class Input(
        val oauthEmail: String,
        val provider: ProviderType,
        val contactEmail: String,
        val univEmail: String,
        val univName: String,
        val name: String,
        val major: String,
        val labInfo: String?,
        var adConsent: Boolean
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
        val contactEmail: String?,
        val role: RoleType?
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
                contactEmail = savedMember.contactEmail,
                role = savedMember.role
            )
        )
    }

    private fun createResearcher(input: Input): Researcher {
        val member = Member.newMember(
            id = idGenerator.generateId(),
            oauthEmail = input.oauthEmail,
            contactEmail = input.contactEmail,
            provider = input.provider,
            role = RoleType.RESEARCHER,
            name = input.name
        )

        val researcher = Researcher.newResearcher(
            id = idGenerator.generateId(),
            member = member,
            univEmail = input.univEmail,
            univName = input.univName,
            emailVerified = true,
            major = input.major,
            labInfo = input.labInfo
        )
        researcherGateway.save(researcher)

        val memberConsent = MemberConsent.newConsent(
            memberId = member.id,
            adConsent = input.adConsent,
            matchConsent = false
        )
        memberConsentGateway.save(memberConsent)

        return researcher
    }
}
