package com.dobby.backend.application.usecase.signupUseCase

import com.dobby.backend.application.mapper.SignupMapper
import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.gateway.ResearcherGateway
import com.dobby.backend.domain.gateway.TokenGateway
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.domain.model.member.Researcher
import com.dobby.backend.infrastructure.database.entity.enum.*
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import com.dobby.backend.infrastructure.database.repository.ResearcherRepository
import com.dobby.backend.infrastructure.token.JwtTokenProvider
import com.dobby.backend.presentation.api.dto.request.signup.ResearcherSignupRequest
import com.dobby.backend.presentation.api.dto.response.MemberResponse
import com.dobby.backend.presentation.api.dto.response.signup.SignupResponse
import com.dobby.backend.util.AuthenticationUtils
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Past
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

class CreateResearcherUseCase(
    private val researcherGateway: ResearcherGateway,
    private val tokenGateway: TokenGateway
) : UseCase<CreateResearcherUseCase.Input, CreateResearcherUseCase.Output> {
    data class Input(
        val oauthEmail: String,
        val provider: ProviderType,
        val contactEmail: String,
        val univEmail : String,
        val emailVerified: Boolean,
        val univName: String,
        val name : String,
        val major: String,
        val labInfo : String?
    )

    data class Output(
        val accessToken: String,
        val refreshToken: String,
        val memberInfo: MemberResponse
    )
    data class MemberResponse(
        val memberId: Long?,
        val name: String?,
        val oauthEmail: String?,
        val provider: ProviderType?,
        val role: RoleType?,
    )

    override fun execute(input: Input):Output {
        val newResearcher = createResearcher(input)
        val accessToken = tokenGateway.generateAccessToken(newResearcher.member)
        val refreshToken = tokenGateway.generateRefreshToken(newResearcher.member)

        return Output(
            accessToken = accessToken,
            refreshToken = refreshToken,
            memberInfo = SignupMapper.modelToResearcherRes(newResearcher)
        )
    }

    private fun createResearcher(input: Input): Researcher {
        val member = Member(
            memberId= 0L,
            status = MemberStatus.ACTIVE,
            oauthEmail = input.oauthEmail,
            contactEmail = input.contactEmail,
            provider = input.provider,
            role = RoleType.RESEARCHER,
            name = input.name
        )

        val researcher = Researcher(
            member = member,
            univEmail = input.univEmail,
            univName = input.univName,
            emailVerified = input.emailVerified,
            major = input.major,
            labInfo = input.labInfo
        )
        researcherGateway.save(researcher)
        return researcher
    }

}
