package com.dobby.backend.presentation.api.mapper

import com.dobby.backend.application.usecase.member.GetResearcherInfoUseCase
import com.dobby.backend.application.usecase.member.CreateResearcherUseCase
import com.dobby.backend.application.usecase.member.CreateParticipantUseCase
import com.dobby.backend.presentation.api.dto.request.signup.ParticipantSignupRequest
import com.dobby.backend.presentation.api.dto.request.signup.ResearcherSignupRequest
import com.dobby.backend.presentation.api.dto.response.expirement.DefaultInfoResponse
import com.dobby.backend.presentation.api.dto.response.member.MemberResponse
import com.dobby.backend.presentation.api.dto.response.member.SignupResponse
import com.dobby.backend.util.getCurrentMemberId

object MemberMapper {
    fun toCreateResearcherInput(req: ResearcherSignupRequest) : CreateResearcherUseCase.Input{
        return CreateResearcherUseCase.Input(
            oauthEmail = req.oauthEmail,
            provider = req.provider,
            contactEmail = req.contactEmail,
            univEmail = req.univEmail,
            emailVerified = req.emailVerified,
            univName = req.univName,
            name = req.name,
            major = req.major,
            labInfo = req.labInfo
        )
    }

    fun toResearcherSignupResponse(output: CreateResearcherUseCase.Output): SignupResponse {
        return SignupResponse(
            accessToken = output.accessToken,
            refreshToken = output.refreshToken,
            memberInfo = toMemberResDto(output.memberInfo)
        )
    }

    fun toCreateParticipantInput(req: ParticipantSignupRequest): CreateParticipantUseCase.Input {
        return CreateParticipantUseCase.Input(
            oauthEmail = req.oauthEmail,
            provider = req.provider,
            contactEmail = req.contactEmail,
            name = req.name,
            gender = req.gender,
            birthDate = req.birthDate,
            basicAddressInfo = CreateParticipantUseCase.AddressInfo(
                region = req.basicAddressInfo.region,
                area = req.basicAddressInfo.area
            ),
            additionalAddressInfo = req.additionalAddressInfo?.let {
                CreateParticipantUseCase.AddressInfo(region = it.region, area = it.area)
            },
            preferType = req.preferType
        )
    }

    fun toParticipantSignupResponse(output: CreateParticipantUseCase.Output): SignupResponse {
        return SignupResponse(
            accessToken = output.accessToken,
            refreshToken = output.refreshToken,
            memberInfo = toMemberResDto(output.memberInfo)
        )
    }

    private fun toMemberResDto(input: Any): MemberResponse {
        return when (input) {
            is CreateResearcherUseCase.MemberResponse -> {
                MemberResponse(
                    memberId = input.memberId,
                    name = input.name,
                    provider = input.provider,
                    role = input.role,
                    oauthEmail = input.oauthEmail
                )
            }
            is CreateParticipantUseCase.MemberResponse -> {
                MemberResponse(
                    memberId = input.memberId,
                    name = input.name,
                    provider = input.provider,
                    role = input.role,
                    oauthEmail = input.oauthEmail
                )
            }
            else -> throw IllegalArgumentException("Unsupported MemberResponse type")
        }
    }

    fun toDefaultInfoUseCaseInput(): GetResearcherInfoUseCase.Input {
        return GetResearcherInfoUseCase.Input(
            memberId = getCurrentMemberId()
        )
    }

    fun toDefaultInfoResponse(response: GetResearcherInfoUseCase.Output): DefaultInfoResponse {
        return DefaultInfoResponse(
            leadResearcher = response.leadResearcher,
            univName = response.univName
        )
    }
}
