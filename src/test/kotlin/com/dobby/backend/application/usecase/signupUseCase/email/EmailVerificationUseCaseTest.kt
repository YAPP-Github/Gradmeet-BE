package com.dobby.backend.application.usecase.signupUseCase.email

import com.dobby.backend.domain.exception.CodeExpiredException
import com.dobby.backend.domain.exception.CodeNotCorrectException
import com.dobby.backend.domain.exception.VerifyInfoNotFoundException
import com.dobby.backend.domain.gateway.VerificationGateway
import com.dobby.backend.domain.model.Verification
import com.dobby.backend.infrastructure.database.entity.enum.VerificationStatus
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import java.time.LocalDateTime

class EmailVerificationUseCaseTest : BehaviorSpec({

})


