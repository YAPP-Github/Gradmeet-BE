package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.ContactEmailDuplicateException
import com.dobby.backend.domain.exception.ResearcherNotFoundException
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.gateway.member.ResearcherGateway
import com.dobby.backend.domain.model.member.Member

class UpdateResearcherInfoUseCase(
    private val researcherGateway: ResearcherGateway,
    private val memberGateway: MemberGateway
) : UseCase<UpdateResearcherInfoUseCase.Input, UpdateResearcherInfoUseCase.Output> {
    data class Input(
        val memberId: String,
        val contactEmail: String,
        val name: String,
        val univName: String,
        val major: String,
        val labInfo: String?
    )

    data class Output(
        val member : Member,
        val univEmail: String,
        val univName: String,
        val major: String,
        val labInfo: String?
    )

    override fun execute(input: Input): Output {
        val researcher = researcherGateway.findByMemberId(input.memberId)
            ?: throw ResearcherNotFoundException
        if (memberGateway.existsByContactEmail(input.contactEmail)) {
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

        return Output(
            member = updatedResearcher.member,
            univEmail = updatedResearcher.univEmail,
            univName = updatedResearcher.univName,
            major = updatedResearcher.major,
            labInfo = updatedResearcher.labInfo
        )
    }
}
