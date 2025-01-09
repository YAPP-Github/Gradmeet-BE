package com.dobby.backend.application.usecase.signupUseCase

import com.dobby.backend.application.mapper.SignupMapper
import com.dobby.backend.domain.gateway.ParticipantGateway
import com.dobby.backend.domain.gateway.TokenGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.model.member.Participant
import com.dobby.backend.infrastructure.database.entity.enum.*
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import java.time.LocalDate

class ParticipantSignupUseCaseTest : BehaviorSpec({

})

