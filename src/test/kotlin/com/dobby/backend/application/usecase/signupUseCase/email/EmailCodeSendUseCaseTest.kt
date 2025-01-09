package com.dobby.backend.application.usecase.signupUseCase.email

import com.dobby.backend.domain.exception.EmailAlreadyVerifiedException
import com.dobby.backend.domain.exception.EmailDomainNotFoundException
import com.dobby.backend.domain.exception.EmailNotUnivException
import com.dobby.backend.domain.gateway.EmailGateway
import com.dobby.backend.domain.gateway.VerificationGateway
import com.dobby.backend.domain.model.Verification
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus
import com.dobby.backend.util.EmailUtils
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import java.time.LocalDateTime

class EmailCodeSendUseCaseTest : BehaviorSpec({



})

