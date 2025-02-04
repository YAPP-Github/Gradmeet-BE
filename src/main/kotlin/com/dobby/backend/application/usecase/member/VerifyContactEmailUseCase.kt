package com.dobby.backend.application.usecase.member

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.SignupContactEmailDuplicateException
import com.dobby.backend.domain.gateway.member.MemberGateway

class VerifyContactEmailUseCase(
    private val memberGateway: MemberGateway
): UseCase<VerifyContactEmailUseCase.Input, VerifyContactEmailUseCase.Output> {
    data class Input(
        val contactEmail: String
    )

    data class Output(
        val success: Boolean
    )

    override fun execute(input: Input): Output {
        if(memberGateway.findByContactEmail(input.contactEmail) == null) return Output(success = true)
        else throw SignupContactEmailDuplicateException
    }
}
