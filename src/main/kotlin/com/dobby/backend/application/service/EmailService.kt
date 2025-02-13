package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.member.email.SendEmailCodeUseCase
import com.dobby.backend.application.usecase.member.email.SendMatchingEmailUseCase
import com.dobby.backend.application.usecase.member.email.VerifyEmailUseCase
import com.dobby.backend.application.usecase.member.email.GetMatchingExperimentPostsUseCase
import jakarta.transaction.Transactional
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val sendEmailCodeUseCase: SendEmailCodeUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val sendMatchingEmailUseCase: SendMatchingEmailUseCase,
    private val getMatchingExperimentPostsUseCase: GetMatchingExperimentPostsUseCase,
    private val transactionExecutor: TransactionExecutor,
    private val dispatcherProvider: CoroutineDispatcherProvider
) {
    private val coroutineScope = CoroutineScope(dispatcherProvider.io + SupervisorJob())
    suspend fun sendEmail(req: SendEmailCodeUseCase.Input): SendEmailCodeUseCase.Output {
        return coroutineScope.async {
            transactionExecutor.execute {
                sendEmailCodeUseCase.execute(req)
            }
        }.await()
    }

    @Transactional
    fun verifyCode(req: VerifyEmailUseCase.Input) : VerifyEmailUseCase.Output {
        return verifyEmailUseCase.execute(req)
    }

    suspend fun sendMatchingEmail(req: SendMatchingEmailUseCase.Input): SendMatchingEmailUseCase.Output {
        return coroutineScope.async {
            transactionExecutor.execute {
                sendMatchingEmailUseCase.execute(req)
            }
        }.await()
    }

    @Transactional
    fun getMatchingInfo(req: GetMatchingExperimentPostsUseCase.Input): GetMatchingExperimentPostsUseCase.Output{
        return getMatchingExperimentPostsUseCase.execute(req)
    }
}
