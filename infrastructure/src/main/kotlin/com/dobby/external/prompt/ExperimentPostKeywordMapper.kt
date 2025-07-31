package com.dobby.external.prompt

import com.dobby.enums.MatchType
import com.dobby.enums.experiment.TimeSlot
import com.dobby.enums.member.GenderType
import com.dobby.external.prompt.dto.ExperimentPostKeywordDto
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
                    startAge = targetGroupDto.startAge,
                    endAge = targetGroupDto.endAge,
                    genderType = targetGroupDto.genderType?.let {
                        try {
                            GenderType.valueOf(it)
                        } catch (e: IllegalArgumentException) {
                            null
                        }
                    },
                    otherCondition = targetGroupDto.otherCondition
                )
            },
            applyMethod = dto.applyMethod?.let { applyMethodDto ->
                ApplyMethodKeyword(
                    content = applyMethodDto.content,
                    isFormUrl = applyMethodDto.isFormUrl,
                    formUrl = applyMethodDto.formUrl,
                    isPhoneNum = applyMethodDto.isPhoneNum,
                    phoneNum = applyMethodDto.phoneNum
                )
            },
            matchType = dto.matchType?.let { matchTypeStr ->
                try {
                    MatchType.valueOf(matchTypeStr)
                } catch (e: IllegalArgumentException) {
                    null
                }
            },
            reward = dto.reward,
            count = dto.count,
            timeRequired = dto.timeRequired?.takeIf { it.isNotBlank() }?.let { timeSlotStr ->
                try {
                    TimeSlot.valueOf(timeSlotStr)
                } catch (e: IllegalArgumentException) {
                    null
                }
            }
        )
    }
}
