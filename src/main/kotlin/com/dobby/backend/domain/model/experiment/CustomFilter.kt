package com.dobby.backend.domain.model.experiment

import com.dobby.backend.infrastructure.database.entity.enums.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region

data class CustomFilter(
    val method: MatchType?,
    val studyTarget: StudyTarget?,
    val locationTarget: LocationTarget?,
    val recruitDone: Boolean?
)
data class StudyTarget(
    val gender: GenderType?,
    val age: Int?
)

data class LocationTarget(
    val region: Region?,
    val areas: List<Area>?
)


