package com.dobby.usecase.member

import com.dobby.usecase.UseCase
import com.dobby.exception.MemberConsentNotFoundException
import com.dobby.exception.ResearcherNotFoundException
import com.dobby.gateway.member.MemberConsentGateway
import com.dobby.gateway.member.ResearcherGateway
import com.dobby.model.member.Member

class GetResearcherInfoUseCase(
    private val researcherGateway: ResearcherGateway,
    private val memberConsentGateway: MemberConsentGateway
) : UseCase<GetResearcherInfoUseCase.Input, GetResearcherInfoUseCase.Output>{
    data class Input(
        val memberId: String
    )

    data class Output(
        val member : Member,
        val univEmail: String,
        val univName: String,
        val major: String,
        val labInfo: String?,
        val adConsent: Boolean
    )

    override fun execute(input: Input): Output {
        val researcher = researcherGateway.findByMemberId(input.memberId)
            ?: throw ResearcherNotFoundException
        val researcherConsent = memberConsentGateway.findByMemberId(input.memberId)
            ?: throw MemberConsentNotFoundException

        return Output(
            member = researcher.member,
            univEmail = researcher.univEmail,
            univName = researcher.univName,
            major = researcher.major,
            labInfo = researcher.labInfo,
            adConsent = researcherConsent.adConsent
        )
    }
}
