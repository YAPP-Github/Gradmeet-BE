package com.dobby.backend.domain.model.experiment

import com.dobby.backend.domain.enums.member.GenderType
import com.dobby.backend.domain.enums.MatchType
import com.dobby.backend.domain.enums.areaInfo.Area
import com.dobby.backend.domain.enums.areaInfo.Region
import com.dobby.backend.domain.enums.experiment.RecruitStatus

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


