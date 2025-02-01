package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.ResearcherNotFoundException
import com.dobby.backend.domain.gateway.member.ResearcherGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.presentation.api.dto.response.member.MemberResponse
import io.swagger.v3.oas.annotations.media.Schema

class GetResearcherInfoUseCase(
    private val researcherGateway: ResearcherGateway
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
    )

    override fun execute(input: Input): Output {
        val researcher = researcherGateway.findByMemberId(input.memberId)
            ?: throw ResearcherNotFoundException

        return Output(
            member = researcher.member,
            univEmail = researcher.univEmail,
            univName = researcher.univName,
            major = researcher.major,
            labInfo = researcher.labInfo,
        )
    }
}
