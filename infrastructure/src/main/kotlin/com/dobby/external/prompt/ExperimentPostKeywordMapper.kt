package com.dobby.external.prompt

import com.dobby.enums.member.GenderType
import com.dobby.model.experiment.keyword.ApplyMethodKeyword
import com.dobby.model.experiment.keyword.ExperimentPostKeyword
import com.dobby.model.experiment.keyword.TargetGroupKeyword
import org.springframework.stereotype.Component

@Component
class ExperimentPostKeywordMapper {

    fun toDomain(dto: ExperimentPostKeywordDto): ExperimentPostKeyword {
        return ExperimentPostKeyword(
            targetGroup = dto.targetGroup?.let {
                TargetGroupKeyword(
                    startAge = it.startAge,
                    endAge = it.endAge,
                    genderType = it.genderType?.let { genderStr ->
                        when(genderStr) {
                            "MALE" -> GenderType.MALE
                            "FEMALE" -> GenderType.FEMALE
                            "ALL" -> GenderType.ALL
                            else -> null
                        }
                    },
                    otherCondition = it.otherCondition
                )
            },
            applyMethod = dto.applyMethod?.let {
                ApplyMethodKeyword(
                    content = it.content,
                    isFormUrl = it.isFormUrl,
                    formUrl = it.formUrl,
                    isPhoneNum = it.isPhoneNum,
                    phoneNum = it.phoneNum
                )
            },
            matchType = dto.matchType,
            reward = dto.reward,
            count = dto.count,
            timeRequired = dto.timeRequired
        )
    }

    fun toDto(domain: ExperimentPostKeyword): ExperimentPostKeywordDto {
        return ExperimentPostKeywordDto(
            targetGroup = domain.targetGroup?.let {
                TargetGroupDto(
                    startAge = it.startAge,
                    endAge = it.endAge,
                    genderType = it.genderType?.name,
                    otherCondition = it.otherCondition
                )
            },
            applyMethod = domain.applyMethod?.let {
                ApplyMethodDto(
                    content = it.content,
                    isFormUrl = it.isFormUrl,
                    formUrl = it.formUrl,
                    isPhoneNum = it.isPhoneNum,
                    phoneNum = it.phoneNum
                )
            },
            matchType = domain.matchType,
            reward = domain.reward,
            count = domain.count,
            timeRequired = domain.timeRequired
        )
    }
}
