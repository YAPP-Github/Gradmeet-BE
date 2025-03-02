package com.dobby.service

import com.dobby.usecase.member.email.SendEmailCodeUseCase
import com.dobby.usecase.member.email.SendMatchingEmailUseCase
import com.dobby.usecase.member.email.VerifyEmailUseCase
import com.dobby.usecase.member.email.GetMatchingExperimentPostsUseCase
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val sendEmailCodeUseCase: SendEmailCodeUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val sendMatchingEmailUseCase: SendMatchingEmailUseCase,
    private val getMatchingExperimentPostsUseCase: GetMatchingExperimentPostsUseCase,
) {

    @Transactional
    fun sendEmail(req: SendEmailCodeUseCase.Input): SendEmailCodeUseCase.Output {
        return sendEmailCodeUseCase.execute(req)
    }

    @Transactional
    fun verifyCode(req: VerifyEmailUseCase.Input) : VerifyEmailUseCase.Output {
        return verifyEmailUseCase.execute(req)
    }

    @Transactional
    fun sendMatchingEmail(req: SendMatchingEmailUseCase.Input): SendMatchingEmailUseCase.Output {
        return sendMatchingEmailUseCase.execute(req)
    }

    @Transactional
    fun getMatchingInfo(req: GetMatchingExperimentPostsUseCase.Input): GetMatchingExperimentPostsUseCase.Output{
        return getMatchingExperimentPostsUseCase.execute(req)
    }
}
