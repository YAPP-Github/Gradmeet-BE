package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.domain.exception.MemberConsentNotFoundException
import com.dobby.domain.exception.ResearcherNotFoundException
import com.dobby.domain.gateway.member.MemberConsentGateway
import com.dobby.domain.gateway.member.ResearcherGateway
import com.dobby.domain.model.member.Member
import com.dobby.backend.presentation.api.dto.response.member.MemberResponse
import io.swagger.v3.oas.annotations.media.Schema

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
