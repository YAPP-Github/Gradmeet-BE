package com.dobby.backend.presentation.api.dto.request.experiment

import com.dobby.backend.infrastructure.database.entity.enums.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import io.swagger.v3.oas.annotations.media.Schema

data class CustomFilter (
    @Schema(description = "진행 방식 필터")
    val matchType : MatchType?,

    @Schema(description = "모집 대상")
    val studyTarget: StudyTarget?,

    @Schema(description = "지역")
    val locationTarget: LocationTarget?,

    @Schema(description = "공고 상태 필터 - false이면 모집 중인 공고만 확인 가능")
    val recruitStatus: Boolean?,
)

data class StudyTarget (
    @Schema(description = "모집 대상 - 성별 필터")
    val gender : GenderType?,

    @Schema(description = "모집 대상 - 나이 필터")
    val age: Int?
)

data class LocationTarget (
    @Schema(description = "지역 - 시/도 필터")
    val region: Region?,

    @Schema(description = "지역 - 시/군/구 필터")
    val areas: List<Area>?,
)
