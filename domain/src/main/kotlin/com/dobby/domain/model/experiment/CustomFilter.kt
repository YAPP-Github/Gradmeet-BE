package com.dobby.domain.model.experiment

import com.dobby.domain.enums.member.GenderType
import com.dobby.domain.enums.MatchType
import com.dobby.domain.enums.areaInfo.Area
import com.dobby.domain.enums.areaInfo.Region
import com.dobby.domain.enums.experiment.RecruitStatus

data class CustomFilter(
    val matchType: MatchType?,
    val studyTarget: StudyTarget?,
    val locationTarget: LocationTarget?,
    val recruitStatus: RecruitStatus
) {
    companion object {
        fun newCustomFilter(
            matchType: MatchType?,
            studyTarget: StudyTarget?,
            locationTarget: LocationTarget?,
            recruitStatus: RecruitStatus
        ): CustomFilter = CustomFilter(matchType, studyTarget, locationTarget, recruitStatus)
    }
}

data class StudyTarget(
    val gender: GenderType?,
    val age: Int?
)

data class LocationTarget(
    val region: Region?,
    val areas: List<Area>?
)


