package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.ResearcherNotFoundException
import com.dobby.backend.domain.gateway.ResearcherGateway

class GetResearcherInfoUseCase(
    private val researcherGateway: ResearcherGateway
) : UseCase<GetResearcherInfoUseCase.Input, GetResearcherInfoUseCase.Output>{
    data class Input(
        val memberId: Long
    )

    data class Output(
        val univName: String,
        val leadResearcher: String,
    )

    override fun execute(input: Input): Output {
        val researcher = researcherGateway.findByMemberId(input.memberId)
            ?: throw ResearcherNotFoundException()

        val leadResearcher = researcher.univName +" "+ researcher.major+
                " " +researcher.labInfo+ " " +researcher.member.name

        return Output(
            univName =researcher.univName,
            leadResearcher = leadResearcher
        )
    }
}
