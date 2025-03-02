package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.exception.ContactEmailDuplicateException
import com.dobby.gateway.member.MemberGateway

class ValidateContactEmailForUpdateUseCase(
    private val memberGateway: MemberGateway
) : UseCase<ValidateContactEmailForUpdateUseCase.Input, ValidateContactEmailForUpdateUseCase.Output> {
    data class Input(
        val memberId: String,
        val contactEmail: String
    )

    data class Output(
        val success: Boolean
    )

    override fun execute(input: Input): Output {
        val currentContactEmail = memberGateway.findContactEmailByMemberId(input.memberId)
        if (currentContactEmail == input.contactEmail || !memberGateway.existsByContactEmail(input.contactEmail))
            return Output(success = true)
        else
            throw ContactEmailDuplicateException
    }
}
