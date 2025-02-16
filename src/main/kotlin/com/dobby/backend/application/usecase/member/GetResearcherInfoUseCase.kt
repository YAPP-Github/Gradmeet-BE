package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.MemberConsentNotFoundException
import com.dobby.backend.domain.exception.ResearcherNotFoundException
import com.dobby.backend.domain.gateway.member.MemberConsentGateway
import com.dobby.backend.domain.gateway.member.ResearcherGateway
import com.dobby.backend.domain.model.member.Member
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
