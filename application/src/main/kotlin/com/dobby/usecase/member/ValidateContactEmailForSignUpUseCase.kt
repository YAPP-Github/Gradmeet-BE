package com.dobby.usecase.member

import com.dobby.exception.ContactEmailDuplicateException
import com.dobby.gateway.member.MemberGateway
import com.dobby.usecase.UseCase

class ValidateContactEmailForSignUpUseCase(
    private val memberGateway: MemberGateway
) : UseCase<ValidateContactEmailForSignUpUseCase.Input, ValidateContactEmailForSignUpUseCase.Output> {
    data class Input(
        val contactEmail: String
    )

    data class Output(
        val success: Boolean
    )

    override fun execute(input: Input): Output {
        if (!memberGateway.existsByContactEmail(input.contactEmail)) {
            return Output(success = true)
        } else {
            throw ContactEmailDuplicateException
        }
    }
}
