package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.ContactEmailDuplicateException
import com.dobby.backend.domain.gateway.member.MemberGateway

class ValidateContactEmailForSignUpUseCase(
    private val memberGateway: MemberGateway
): UseCase<ValidateContactEmailForSignUpUseCase.Input, ValidateContactEmailForSignUpUseCase.Output> {
    data class Input(
        val contactEmail: String
    )

    data class Output(
        val success: Boolean
    )

    override fun execute(input: Input): Output {
        if(!memberGateway.existsByContactEmail(input.contactEmail))
            return Output(success = true)
        else
            throw ContactEmailDuplicateException
    }
}
