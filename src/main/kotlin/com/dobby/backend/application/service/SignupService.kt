package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.signupUseCase.ParticipantSignupUseCase
import com.dobby.backend.application.usecase.signupUseCase.CreateResearcherUseCase
import com.dobby.backend.application.usecase.signupUseCase.VerifyResearcherEmailUseCase
import com.dobby.backend.domain.exception.EmailNotValidateException
import com.dobby.backend.domain.exception.SignupOauthEmailDuplicateException
import com.dobby.backend.domain.gateway.MemberGateway
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SignupService(
    private val memberGateway: MemberGateway,
    private val participantSignupUseCase: ParticipantSignupUseCase,
    private val createResearcherUseCase: CreateResearcherUseCase,
    private val verifyResearcherEmailUseCase: VerifyResearcherEmailUseCase
) {
    @Transactional
    fun participantSignup(input: ParticipantSignupUseCase.Input): ParticipantSignupUseCase.Output {
           return participantSignupUseCase.execute(input)
    }

    @Transactional
    fun researcherSignup(input: CreateResearcherUseCase.Input) : CreateResearcherUseCase.Output{
        if(!input.emailVerified) {
            throw EmailNotValidateException()
        }
        val existingMember = memberGateway.findByOauthEmailAndStatus(input.oauthEmail, MemberStatus.ACTIVE)
        if(existingMember!= null) throw SignupOauthEmailDuplicateException()

        verifyResearcherEmailUseCase.execute(input.univEmail)
        return createResearcherUseCase.execute(input)
    }

}
