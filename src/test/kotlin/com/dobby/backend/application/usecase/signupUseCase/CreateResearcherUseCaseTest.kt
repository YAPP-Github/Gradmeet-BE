package com.dobby.backend.application.usecase.signupUseCase

import com.dobby.backend.application.mapper.SignupMapper
import com.dobby.backend.domain.gateway.ResearcherGateway
import com.dobby.backend.domain.gateway.TokenGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.model.member.Researcher
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class CreateResearcherUseCaseTest : BehaviorSpec({

})
