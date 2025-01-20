package com.dobby.backend.presentation.api.mapper

import com.dobby.backend.application.usecase.member.*
import com.dobby.backend.application.usecase.member.GetResearcherInfoUseCase
import com.dobby.backend.application.usecase.member.CreateResearcherUseCase
import com.dobby.backend.application.usecase.member.CreateParticipantUseCase
import com.dobby.backend.application.usecase.member.GetParticipantInfoUseCase
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import com.dobby.backend.presentation.api.dto.response.PaginatedResponse
import com.dobby.backend.presentation.api.dto.request.member.ParticipantSignupRequest
import com.dobby.backend.presentation.api.dto.request.member.ResearcherSignupRequest
import com.dobby.backend.presentation.api.dto.response.member.*
import com.dobby.backend.util.getCurrentMemberId

object MemberMapper {
    fun toCreateResearcherInput(req: ResearcherSignupRequest) : CreateResearcherUseCase.Input{
        return CreateResearcherUseCase.Input(
            oauthEmail = req.oauthEmail,
            provider = req.provider,
            contactEmail = req.contactEmail,
            univEmail = req.univEmail,
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
            additionalAddressInfo = CreateParticipantUseCase.AddressInfo(
                region = req.additionalAddressInfo?.region ?: Region.NONE,
                area = req.additionalAddressInfo?.area ?: Area.NONE
            ),
            matchType = req.matchType
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

    fun toGetResearcherInfoUseCaseInput(): GetResearcherInfoUseCase.Input {
        return GetResearcherInfoUseCase.Input(
            memberId = getCurrentMemberId()
        )
    }

    fun toResearcherInfoResponse(response: GetResearcherInfoUseCase.Output): ResearcherInfoResponse {
        return ResearcherInfoResponse(
            leadResearcher = response.leadResearcher,
            univName = response.univName
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
            matchType = output.matchType
        )
    }

    fun toGetMyExperimentPosts(
        pagination: GetMyExperimentPostsUseCase.PaginationInput
    ): GetMyExperimentPostsUseCase.Input {
        return GetMyExperimentPostsUseCase.Input(
            memberId = getCurrentMemberId(),
            pagination = pagination
        )
    }

    fun toGetMyExperimentPostsResponse(
        output: List<GetMyExperimentPostsUseCase.Output>,
        page: Int,
        totalCount: Int,
        isLast: Boolean
    ): PaginatedResponse<MyExperimentPostResponse> {
        return PaginatedResponse(
            content = output.map { post ->
                MyExperimentPostResponse(
                    experimentPostId = post.experimentPostId,
                    title = post.title,
                    content = post.content,
                    views = post.views,
                    recruitStatus = post.recruitStatus,
                    uploadDate = post.uploadDate
                )
            },
            page = page,
            size = output.size,
            totalCount = totalCount,
            isLast = isLast
        )
    }

    fun toUseCasePagination(
        page: Int, count: Int, order: String
    ) : GetMyExperimentPostsUseCase.PaginationInput {
        return GetMyExperimentPostsUseCase.PaginationInput(
            page = page,
            count = count,
            order = order
        )
    }

    fun toGetTotalMyExperimentPostCountUseCaseInput(): GetMyExperimentPostTotalCountUseCase.Input {
        return GetMyExperimentPostTotalCountUseCase.Input(
            memberId = getCurrentMemberId()
        )
    }

    fun toUpdateExperimentPostRecruitStatusUseCaseInput(postId: Long): UpdateExperimentPostRecruitStatusUseCase.Input {
        return UpdateExperimentPostRecruitStatusUseCase.Input(
            memberId = getCurrentMemberId(),
            postId = postId
        )
    }

    fun toMyExperimentPostResponse(output: UpdateExperimentPostRecruitStatusUseCase.Output): MyExperimentPostResponse {
        return output.experimentPost.let {
            MyExperimentPostResponse(
                experimentPostId = it.id,
                title = it.title,
                content = it.content,
                views = it.views,
                recruitStatus = it.recruitStatus,
                uploadDate = it.createdAt.toLocalDate()
            )
        }
    }
}
