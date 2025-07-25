package com.dobby.api.mapper

import com.dobby.api.dto.request.member.DeleteMemberRequest
import com.dobby.api.dto.request.member.ParticipantSignupRequest
import com.dobby.api.dto.request.member.ResearcherSignupRequest
import com.dobby.api.dto.request.member.SearchAutoCompleteRequest
import com.dobby.api.dto.request.member.UpdateParticipantInfoRequest
import com.dobby.api.dto.request.member.UpdateResearcherInfoRequest
import com.dobby.api.dto.response.member.AddressInfoResponse
import com.dobby.api.dto.response.member.AutoCompleteResponse
import com.dobby.api.dto.response.member.DefaultResponse
import com.dobby.api.dto.response.member.MemberResponse
import com.dobby.api.dto.response.member.ParticipantInfoResponse
import com.dobby.api.dto.response.member.ResearcherInfoResponse
import com.dobby.api.dto.response.member.SignUpResponse
import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import com.dobby.enums.member.RoleType
import com.dobby.model.member.Participant
import com.dobby.usecase.member.CreateParticipantUseCase
import com.dobby.usecase.member.CreateResearcherUseCase
import com.dobby.usecase.member.DeleteParticipantUseCase
import com.dobby.usecase.member.DeleteResearcherUseCase
import com.dobby.usecase.member.GetParticipantInfoUseCase
import com.dobby.usecase.member.GetResearcherInfoUseCase
import com.dobby.usecase.member.SearchUniversityAutoCompleteUseCase
import com.dobby.usecase.member.UpdateParticipantInfoUseCase
import com.dobby.usecase.member.UpdateResearcherInfoUseCase
import com.dobby.usecase.member.ValidateContactEmailForSignUpUseCase
import com.dobby.usecase.member.ValidateContactEmailForUpdateUseCase
import com.dobby.util.getCurrentMemberId

object MemberMapper {
    fun toCreateResearcherInput(req: ResearcherSignupRequest): CreateResearcherUseCase.Input {
        return CreateResearcherUseCase.Input(
            oauthEmail = req.oauthEmail,
            provider = req.provider,
            contactEmail = req.contactEmail,
            univEmail = req.univEmail,
            univName = req.univName,
            name = req.name,
            major = req.major,
            labInfo = req.labInfo,
            adConsent = req.adConsent
        )
    }

    fun toResearcherSignUpResponse(output: CreateResearcherUseCase.Output): SignUpResponse {
        return SignUpResponse(
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
            additionalAddressInfo = CreateParticipantUseCase.AddressInfo(
                region = req.additionalAddressInfo?.region ?: Region.NONE,
                area = req.additionalAddressInfo?.area ?: Area.NONE
            ),
            matchType = req.matchType,
            adConsent = req.adConsent,
            matchConsent = req.matchConsent
        )
    }

    fun toParticipantSignUpResponse(output: CreateParticipantUseCase.Output): SignUpResponse {
        return SignUpResponse(
            accessToken = output.accessToken,
            refreshToken = output.refreshToken,
            memberInfo = toMemberResDto(output.memberInfo)
        )
    }

    fun toValidateContactEmailForSignUpInput(contactEmail: String): ValidateContactEmailForSignUpUseCase.Input {
        return ValidateContactEmailForSignUpUseCase.Input(
            contactEmail = contactEmail
        )
    }

    fun toValidateContactEmailForSignUpResponse(output: ValidateContactEmailForSignUpUseCase.Output): DefaultResponse {
        return DefaultResponse(
            success = output.success
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
                    oauthEmail = input.oauthEmail,
                    contactEmail = input.contactEmail
                )
            }
            is CreateParticipantUseCase.MemberResponse -> {
                MemberResponse(
                    memberId = input.memberId,
                    name = input.name,
                    provider = input.provider,
                    role = input.role,
                    oauthEmail = input.oauthEmail,
                    contactEmail = input.contactEmail
                )
            }
            else -> throw IllegalArgumentException("Unsupported MemberResponse type")
        }
    }

    fun toGetResearcherInfoUseCaseInput(): GetResearcherInfoUseCase.Input {
        return GetResearcherInfoUseCase.Input(
            memberId = getCurrentMemberId()
        )
    }

    fun toResearcherInfoResponse(response: GetResearcherInfoUseCase.Output): ResearcherInfoResponse {
        return ResearcherInfoResponse(
            memberInfo = MemberResponse.fromDomain(response.member),
            univEmail = response.univEmail,
            univName = response.univName,
            major = response.major,
            labInfo = response.labInfo,
            adConsent = response.adConsent
        )
    }

    fun toGetParticipantInfoUseCaseInput(): GetParticipantInfoUseCase.Input {
        return GetParticipantInfoUseCase.Input(
            memberId = getCurrentMemberId()
        )
    }

    fun toParticipantInfoResponse(output: GetParticipantInfoUseCase.Output): ParticipantInfoResponse {
        return ParticipantInfoResponse(
            memberInfo = MemberResponse.fromDomain(output.member),
            gender = output.gender,
            birthDate = output.birthDate,
            basicAddressInfo = AddressInfoResponse.fromDomain(output.basicAddressInfo),
            additionalAddressInfo = output.additionalAddressInfo?.let { AddressInfoResponse.fromDomain(it) },
            matchType = output.matchType,
            adConsent = output.adConsent,
            matchConsent = output.matchConsent
        )
    }

    fun toUpdateResearcherInfoUseCaseInput(request: UpdateResearcherInfoRequest): UpdateResearcherInfoUseCase.Input {
        return UpdateResearcherInfoUseCase.Input(
            memberId = getCurrentMemberId(),
            contactEmail = request.contactEmail,
            name = request.name,
            univName = request.univName,
            major = request.major,
            labInfo = request.labInfo,
            adConsent = request.adConsent
        )
    }

    fun toResearcherInfoResponse(response: UpdateResearcherInfoUseCase.Output): ResearcherInfoResponse {
        return ResearcherInfoResponse(
            memberInfo = MemberResponse.fromDomain(response.member),
            univEmail = response.univEmail,
            univName = response.univName,
            major = response.major,
            labInfo = response.labInfo,
            adConsent = response.adConsent
        )
    }

    fun toUpdateParticipantInfoUseCaseInput(request: UpdateParticipantInfoRequest): UpdateParticipantInfoUseCase.Input {
        return UpdateParticipantInfoUseCase.Input(
            memberId = getCurrentMemberId(),
            contactEmail = request.contactEmail,
            name = request.name,
            basicAddressInfo = Participant.AddressInfo(
                region = request.basicAddressInfo.region,
                area = request.basicAddressInfo.area
            ),
            additionalAddressInfo = Participant.AddressInfo(
                region = request.additionalAddressInfo?.region ?: Region.NONE,
                area = request.additionalAddressInfo?.area ?: Area.NONE
            ),
            matchType = request.matchType,
            adConsent = request.adConsent,
            matchConsent = request.matchConsent
        )
    }

    fun toParticipantInfoResponse(output: UpdateParticipantInfoUseCase.Output): ParticipantInfoResponse {
        return ParticipantInfoResponse(
            memberInfo = MemberResponse.fromDomain(output.member),
            gender = output.gender,
            birthDate = output.birthDate,
            basicAddressInfo = AddressInfoResponse.fromDomain(output.basicAddressInfo),
            additionalAddressInfo = output.additionalAddressInfo?.let { AddressInfoResponse.fromDomain(it) },
            matchType = output.matchType,
            adConsent = output.adConsent,
            matchConsent = output.matchConsent
        )
    }

    fun toValidateContactEmailForUpdateUseCaseInput(email: String): ValidateContactEmailForUpdateUseCase.Input {
        return ValidateContactEmailForUpdateUseCase.Input(
            memberId = getCurrentMemberId(),
            contactEmail = email
        )
    }

    fun toValidateContactEmailForUpdateResponse(output: ValidateContactEmailForUpdateUseCase.Output): DefaultResponse {
        return DefaultResponse(
            success = output.success
        )
    }

    fun toDeleteMemberUseCaseInput(request: DeleteMemberRequest, roleType: RoleType): Any {
        return when (roleType) {
            RoleType.RESEARCHER -> DeleteResearcherUseCase.Input(
                memberId = getCurrentMemberId(),
                reasonType = request.reasonType,
                reason = request.reason
            )
            RoleType.PARTICIPANT -> DeleteParticipantUseCase.Input(
                memberId = getCurrentMemberId(),
                reasonType = request.reasonType,
                reason = request.reason
            )
        }
    }

    fun toSearchUniversityAutoCompleteUseCaseInput(request: SearchAutoCompleteRequest): SearchUniversityAutoCompleteUseCase.Input {
        return SearchUniversityAutoCompleteUseCase.Input(
            query = request.query
        )
    }

    fun toAutoCompleteResponse(output: SearchUniversityAutoCompleteUseCase.Output): AutoCompleteResponse {
        return AutoCompleteResponse(
            result = output.output
        )
    }
}
