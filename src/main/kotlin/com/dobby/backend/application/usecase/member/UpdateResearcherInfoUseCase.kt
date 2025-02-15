package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.ContactEmailDuplicateException
import com.dobby.backend.domain.exception.MemberConsentNotFoundException
import com.dobby.backend.domain.exception.ResearcherNotFoundException
import com.dobby.backend.domain.gateway.member.MemberConsentGateway
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.gateway.member.ResearcherGateway
import com.dobby.backend.domain.model.member.Member
import io.swagger.v3.oas.annotations.media.Schema

class UpdateResearcherInfoUseCase(
    private val researcherGateway: ResearcherGateway,
    private val memberGateway: MemberGateway,
    private val memberConsentGateway: MemberConsentGateway
) : UseCase<UpdateResearcherInfoUseCase.Input, UpdateResearcherInfoUseCase.Output> {
    data class Input(
        val memberId: String,
        val contactEmail: String,
        val name: String,
        val univName: String,
        val major: String,
        val labInfo: String?,
        var adConsent: Boolean,
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
        if (memberGateway.existsByContactEmail(input.contactEmail) && researcher.member.contactEmail != input.contactEmail) {
            throw ContactEmailDuplicateException
        }

        val updatedResearcher = researcherGateway.save(
            researcher.updateInfo(
                contactEmail = input.contactEmail,
                name = input.name,
                univName = input.univName,
                major = input.major,
                labInfo = input.labInfo
            )
        )

        val updatedConsent = memberConsentGateway.save(
            researcherConsent.update(
                memberId = updatedResearcher.member.id,
                adConsent = input.adConsent,
                matchConsent = false
            )
        )

        return Output(
            member = updatedResearcher.member,
            univEmail = updatedResearcher.univEmail,
            univName = updatedResearcher.univName,
            major = updatedResearcher.major,
            labInfo = updatedResearcher.labInfo,
            adConsent = updatedConsent.adConsent
        )
    }
}
