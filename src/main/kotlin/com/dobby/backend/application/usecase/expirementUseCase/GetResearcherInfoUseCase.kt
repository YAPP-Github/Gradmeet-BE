package com.dobby.backend.application.usecase.expirementUseCase

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.ResearcherNotFoundException
import com.dobby.backend.domain.gateway.ResearcherGateway
import com.dobby.backend.util.AuthenticationUtils

class GetResearcherInfoUseCase(
    private val researcherGateway: ResearcherGateway
) : UseCase<Unit, GetResearcherInfoUseCase.Output>{
    data class Output(
        val univName: String,
        val researcherName: String,
    )

    override fun execute(input: Unit): Output {
        val memberId = AuthenticationUtils.getCurrentMemberId()
        val researcher = researcherGateway.findByMemberId(memberId)
            ?: throw ResearcherNotFoundException()

        val researcherName = researcher.univName +" "+ researcher.major+
                " " +researcher.labInfo+ " " +researcher.member.name

        return Output(
            univName =researcher.univName,
            researcherName = researcherName
        )
    }
}
