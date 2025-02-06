package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.gateway.member.MemberGateway

class ValidateContactEmailForUpdateUseCase(
    private val memberGateway: MemberGateway
) : UseCase<ValidateContactEmailForUpdateUseCase.Input, ValidateContactEmailForUpdateUseCase.Output> {
    data class Input(
        val memberId: String,
        val contactEmail: String
    )

    data class Output(
        val isDuplicate: Boolean
    )

    override fun execute(input: Input): Output {
        val currentContactEmail = memberGateway.findContactEmailByMemberId(input.memberId)
        if (currentContactEmail == input.contactEmail) {
            return Output(false)
        }

        val isDuplicate = memberGateway.existsByContactEmail(input.contactEmail)
        return Output(isDuplicate)
    }
}
