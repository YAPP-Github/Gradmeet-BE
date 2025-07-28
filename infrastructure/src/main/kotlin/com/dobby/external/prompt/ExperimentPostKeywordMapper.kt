package com.dobby.external.prompt

import com.dobby.enums.MatchType
import com.dobby.enums.experiment.TimeSlot
import com.dobby.enums.member.GenderType
import com.dobby.external.prompt.dto.ApplyMethodDto
import com.dobby.external.prompt.dto.ExperimentPostKeywordDto
import com.dobby.external.prompt.dto.TargetGroupDto
import com.dobby.model.experiment.keyword.ApplyMethodKeyword
import com.dobby.model.experiment.keyword.ExperimentPostKeywords
import com.dobby.model.experiment.keyword.TargetGroupKeyword
import org.springframework.stereotype.Component

@Component
class ExperimentPostKeywordMapper {

    fun toDomain(dto: ExperimentPostKeywordDto): ExperimentPostKeywords {
        return ExperimentPostKeywords(
            targetGroup = dto.targetGroup?.let { targetGroupDto ->
                TargetGroupKeyword(
                    startAge = targetGroupDto.startAge ?: 0,
                    endAge = targetGroupDto.endAge ?: 0,
                    genderType = targetGroupDto.genderType?.let { genderStr ->
                        when (genderStr) {
                            "MALE" -> GenderType.MALE
                            "FEMALE" -> GenderType.FEMALE
                            "ALL" -> GenderType.ALL
                            else -> GenderType.ALL
                        }
                    } ?: GenderType.ALL,
                    otherCondition = targetGroupDto.otherCondition
                )
            },
            applyMethod = dto.applyMethod?.let { applyMethodDto ->
                ApplyMethodKeyword(
                    content = applyMethodDto.content ?: "",
                    isFormUrl = applyMethodDto.isFormUrl,
                    formUrl = applyMethodDto.formUrl ?: "",
                    isPhoneNum = applyMethodDto.isPhoneNum,
                    phoneNum = applyMethodDto.phoneNum ?: ""
                )
            },
            matchType = dto.matchType?.let { matchTypeStr ->
                try {
                    MatchType.valueOf(matchTypeStr)
                } catch (e: IllegalArgumentException) {
                    MatchType.ALL
                }
            } ?: MatchType.ALL,
            reward = dto.reward ?: "",
            count = dto.count ?: 0,
            timeRequired = dto.timeRequired?.takeIf { it.isNotBlank() }?.let { timeSlotStr ->
                try {
                    TimeSlot.valueOf(timeSlotStr)
                } catch (e: IllegalArgumentException) {
                    null
                }
            }
        )
    }

    fun toDto(domain: ExperimentPostKeywords): ExperimentPostKeywordDto {
        return ExperimentPostKeywordDto(
            targetGroup = domain.targetGroup?.let { targetGroupDomain ->
                TargetGroupDto(
                    startAge = targetGroupDomain.startAge,
                    endAge = targetGroupDomain.endAge,
                    genderType = targetGroupDomain.genderType?.name,
                    otherCondition = targetGroupDomain.otherCondition
                )
            },
            applyMethod = domain.applyMethod?.let { applyMethodDomain ->
                ApplyMethodDto(
                    content = applyMethodDomain.content,
                    isFormUrl = applyMethodDomain.isFormUrl,
                    formUrl = applyMethodDomain.formUrl,
                    isPhoneNum = applyMethodDomain.isPhoneNum,
                    phoneNum = applyMethodDomain.phoneNum
                )
            },
            matchType = domain.matchType?.name,
            reward = domain.reward,
            count = domain.count,
            timeRequired = domain.timeRequired?.name
        )
    }
}
