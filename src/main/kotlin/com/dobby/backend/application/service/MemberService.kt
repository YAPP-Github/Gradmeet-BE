package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.member.GetMyExperimentPostTotalCountUseCase
import com.dobby.backend.application.usecase.member.*
import com.dobby.backend.domain.exception.SignupOauthEmailDuplicateException
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.infrastructure.database.entity.enums.MemberStatus
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.security.InvalidParameterException

@Service
class MemberService(
    private val memberGateway: MemberGateway,
    private val createParticipantUseCase: CreateParticipantUseCase,
    private val createResearcherUseCase: CreateResearcherUseCase,
    private val verifyResearcherEmailUseCase: VerifyResearcherEmailUseCase,
    private val getResearcherInfoUseCase: GetResearcherInfoUseCase,
    private val getParticipantInfoUseCase: GetParticipantInfoUseCase,
    private val getMyExperimentPostsUseCase: GetMyExperimentPostsUseCase,
    private val getMyExperimentPostTotalCountUseCase: GetMyExperimentPostTotalCountUseCase
) {
    @Transactional
    fun participantSignup(input: CreateParticipantUseCase.Input): CreateParticipantUseCase.Output {
           return createParticipantUseCase.execute(input)
    }

    @Transactional
    fun researcherSignup(input: CreateResearcherUseCase.Input) : CreateResearcherUseCase.Output{
        val existingMember = memberGateway.findByOauthEmailAndStatus(input.oauthEmail, MemberStatus.ACTIVE)
        if(existingMember!= null) throw SignupOauthEmailDuplicateException()

        verifyResearcherEmailUseCase.execute(input.univEmail)
        return createResearcherUseCase.execute(input)
    }

    fun getResearcherInfo(input: GetResearcherInfoUseCase.Input): GetResearcherInfoUseCase.Output {
        return getResearcherInfoUseCase.execute(input)
    }

    @Transactional
    fun getParticipantInfo(input: GetParticipantInfoUseCase.Input): GetParticipantInfoUseCase.Output {
        return getParticipantInfoUseCase.execute(input)
    }

    fun getMyExperimentPosts(input: GetMyExperimentPostsUseCase.Input): List<GetMyExperimentPostsUseCase.Output> {
        validateSortOrder(input.pagination.order)
        return getMyExperimentPostsUseCase.execute(input)
    }

    private fun validateSortOrder(sortOrder: String): String {
        return when (sortOrder) {
            "ASC", "DESC" -> sortOrder
            else -> throw InvalidParameterException("Invalid sort order. Please use 'ASC' or 'DESC'")
        }
    }

    fun getMyExperimentPostsCount(input: GetMyExperimentPostTotalCountUseCase.Input): GetMyExperimentPostTotalCountUseCase.Output {
        return getMyExperimentPostTotalCountUseCase.execute(GetMyExperimentPostTotalCountUseCase.Input(input.memberId))
    }
}
