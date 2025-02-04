package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.member.email.SendEmailCodeUseCase
import com.dobby.backend.application.usecase.member.email.SendMatcingEmailUseCase
import com.dobby.backend.application.usecase.member.email.VerifyEmailUseCase
import com.dobby.backend.application.usecase.member.email.GetMatchingExperimentPostsUseCase
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val sendEmailCodeUseCase: SendEmailCodeUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val sendMatcingEmailUseCase: SendMatcingEmailUseCase,
    private val getMatchingExperimentPostsUseCase: GetMatchingExperimentPostsUseCase
) {
    @Transactional
    fun sendEmail(req: SendEmailCodeUseCase.Input) : SendEmailCodeUseCase.Output{
        return sendEmailCodeUseCase.execute(req)
    }

    @Transactional
    fun verifyCode(req: VerifyEmailUseCase.Input) : VerifyEmailUseCase.Output {
        return verifyEmailUseCase.execute(req)
    }

    @Transactional
    fun sendMatchingEmail(req: SendMatcingEmailUseCase.Input): SendMatcingEmailUseCase.Output{
        return sendMatcingEmailUseCase.execute(req)
    }

    @Transactional
    fun getMatchingInfo(req: GetMatchingExperimentPostsUseCase.Input): GetMatchingExperimentPostsUseCase.Output{
        return getMatchingExperimentPostsUseCase.execute(req)
    }
}
