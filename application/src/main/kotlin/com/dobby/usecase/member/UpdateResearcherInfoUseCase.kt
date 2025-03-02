package com.dobby.usecase.member

import com.dobby.usecase.UseCase
import com.dobby.exception.ContactEmailDuplicateException
import com.dobby.exception.MemberConsentNotFoundException
import com.dobby.exception.ResearcherNotFoundException
import com.dobby.gateway.member.MemberConsentGateway
import com.dobby.gateway.member.MemberGateway
import com.dobby.gateway.member.ResearcherGateway
import com.dobby.model.member.Member

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
        val adConsent: Boolean,
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
