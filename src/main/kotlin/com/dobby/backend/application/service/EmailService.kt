package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.member.email.EmailCodeSendUseCase
import com.dobby.backend.application.usecase.member.email.EmailMatchSendUseCase
import com.dobby.backend.application.usecase.member.email.EmailVerificationUseCase
import com.dobby.backend.application.usecase.member.email.GetMatchingExperimentPostsUseCase
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val emailCodeSendUseCase: EmailCodeSendUseCase,
    private val emailVerificationUseCase: EmailVerificationUseCase,
    private val emailMatchSendUseCase: EmailMatchSendUseCase,
    private val getMatchingExperimentPostsUseCase: GetMatchingExperimentPostsUseCase
) {
    @Transactional
    fun sendEmail(req: EmailCodeSendUseCase.Input) : EmailCodeSendUseCase.Output{
        return emailCodeSendUseCase.execute(req)
    }

    @Transactional
    fun verifyCode(req: EmailVerificationUseCase.Input) : EmailVerificationUseCase.Output {
        return emailVerificationUseCase.execute(req)
    }

    @Transactional
    fun sendMatchingEmail(req: EmailMatchSendUseCase.Input): EmailMatchSendUseCase.Output{
        return emailMatchSendUseCase.execute(req)
    }

    @Transactional
    fun getMatchingInfo(req: GetMatchingExperimentPostsUseCase.Input): GetMatchingExperimentPostsUseCase.Output{
        return getMatchingExperimentPostsUseCase.execute(req)
    }
}
