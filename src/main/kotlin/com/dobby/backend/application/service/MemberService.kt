package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.member.CreateParticipantUseCase
import com.dobby.backend.application.usecase.member.CreateResearcherUseCase
import com.dobby.backend.application.usecase.member.VerifyResearcherEmailUseCase
import com.dobby.backend.domain.exception.SignupOauthEmailDuplicateException
import com.dobby.backend.domain.gateway.MemberGateway
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberGateway: MemberGateway,
    private val createParticipantUseCase: CreateParticipantUseCase,
    private val createResearcherUseCase: CreateResearcherUseCase,
    private val verifyResearcherEmailUseCase: VerifyResearcherEmailUseCase
) {
    @Transactional
    fun participantSignup(input: CreateParticipantUseCase.Input): CreateParticipantUseCase.Output {
           return createParticipantUseCase.execute(input)
    }

    @Transactional
    fun researcherSignup(input: CreateResearcherUseCase.Input) : CreateResearcherUseCase.Output{
        val existingMember = memberGateway.findByOauthEmailAndStatus(input.oauthEmail, MemberStatus.ACTIVE)
        if(existingMember!= null) throw SignupOauthEmailDuplicateException()

        verifyResearcherEmailUseCase.execute(input.univEmail)
        return createResearcherUseCase.execute(input)
    }

}
