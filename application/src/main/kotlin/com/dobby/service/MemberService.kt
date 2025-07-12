package com.dobby.service

import com.dobby.enums.member.MemberStatus
import com.dobby.enums.member.RoleType
import com.dobby.exception.MemberNotFoundException
import com.dobby.exception.SignupOauthEmailDuplicateException
import com.dobby.gateway.member.MemberGateway
import com.dobby.usecase.member.*
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberGateway: MemberGateway,
    private val createParticipantUseCase: CreateParticipantUseCase,
    private val createResearcherUseCase: CreateResearcherUseCase,
    private val verifyContactEmailUseCase: ValidateContactEmailForSignUpUseCase,
    private val verifyResearcherEmailUseCase: VerifyResearcherEmailUseCase,
    private val getResearcherInfoUseCase: GetResearcherInfoUseCase,
    private val getParticipantInfoUseCase: GetParticipantInfoUseCase,
    private val updateResearcherInfoUseCase: UpdateResearcherInfoUseCase,
    private val updateParticipantInfoUseCase: UpdateParticipantInfoUseCase,
    private val validateContactEmailForUpdateUseCase: ValidateContactEmailForUpdateUseCase,
    private val deleteParticipantUseCase: DeleteParticipantUseCase,
    private val deleteResearcherUseCase: DeleteResearcherUseCase,
    private val searchUniversityAutoCompleteUseCase: SearchUniversityAutoCompleteUseCase
) {
    @Transactional
    fun signUpParticipant(input: CreateParticipantUseCase.Input): CreateParticipantUseCase.Output {
        val existingMember = memberGateway.findByOauthEmailAndStatus(input.oauthEmail, MemberStatus.ACTIVE)
        if (existingMember != null) throw SignupOauthEmailDuplicateException
        return createParticipantUseCase.execute(input)
    }

    @Transactional
    fun signUpResearcher(input: CreateResearcherUseCase.Input): CreateResearcherUseCase.Output {
        val existingMember = memberGateway.findByOauthEmailAndStatus(input.oauthEmail, MemberStatus.ACTIVE)
        if (existingMember != null) throw SignupOauthEmailDuplicateException

        verifyResearcherEmailUseCase.execute(input.univEmail)
        return createResearcherUseCase.execute(input)
    }

    @Transactional
    fun validateContactEmailForSignUp(input: ValidateContactEmailForSignUpUseCase.Input): ValidateContactEmailForSignUpUseCase.Output {
        return verifyContactEmailUseCase.execute(input)
    }

    @Transactional
    fun getResearcherInfo(input: GetResearcherInfoUseCase.Input): GetResearcherInfoUseCase.Output {
        return getResearcherInfoUseCase.execute(input)
    }

    @Transactional
    fun getParticipantInfo(input: GetParticipantInfoUseCase.Input): GetParticipantInfoUseCase.Output {
        return getParticipantInfoUseCase.execute(input)
    }

    @Transactional
    fun updateResearcherInfo(input: UpdateResearcherInfoUseCase.Input): UpdateResearcherInfoUseCase.Output {
        return updateResearcherInfoUseCase.execute(input)
    }

    @Transactional
    fun updateParticipantInfo(input: UpdateParticipantInfoUseCase.Input): UpdateParticipantInfoUseCase.Output {
        return updateParticipantInfoUseCase.execute(input)
    }

    fun validateContactEmailForUpdate(input: ValidateContactEmailForUpdateUseCase.Input): ValidateContactEmailForUpdateUseCase.Output {
        return validateContactEmailForUpdateUseCase.execute(input)
    }

    fun getAutoCompleteListForUniversities(input: SearchUniversityAutoCompleteUseCase.Input): SearchUniversityAutoCompleteUseCase.Output {
        return searchUniversityAutoCompleteUseCase.execute(input)
    }

    @Transactional
    fun deleteMember(input: Any): Any {
        return when (input) {
            is DeleteParticipantUseCase.Input -> deleteParticipantUseCase.execute(input)
            is DeleteResearcherUseCase.Input -> deleteResearcherUseCase.execute(input)
            else -> throw IllegalArgumentException("Unsupported DeleteMember input type")
        }
    }

    fun getMemberRole(memberId: String): RoleType {
        return memberGateway.findRoleByIdAndDeletedAtIsNull(memberId)
            ?: throw MemberNotFoundException
    }

}
