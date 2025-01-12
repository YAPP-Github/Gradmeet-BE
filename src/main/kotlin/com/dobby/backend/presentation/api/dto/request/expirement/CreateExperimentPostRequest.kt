package com.dobby.backend.presentation.api.dto.request.expirement

import com.dobby.backend.infrastructure.database.entity.enum.GenderType
import com.dobby.backend.infrastructure.database.entity.enum.MatchType
import com.dobby.backend.infrastructure.database.entity.enum.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import java.time.LocalDate

data class CreateExperimentPostRequest(
    val targetGroupInfo: TargetGroupInfo,
    val applyMethodInfo: ApplyMethodInfo,
    val imageListInfo: ImageListInfo,

    val startDate: LocalDate,
    val endDate: LocalDate,
    val matchType: MatchType,
    val count: Int, // N 회 참여
    val timeRequired: TimeSlot,

    val leadResearcher: String, // 연구 책임 정보 -> 기본값: 연구자 정보에서 끌어와야 함, 추후에 자유롭게 수정 가능

    val univName: String, // 대학교 이름 -> 기본값: 연구자 정보에서 끌어와야 함, 추후에 자유롭게 수정 가능
    val region: Region,
    val area: Area,
    val detailedAddress: String?,

    val reward: String,
    val title: String,
    val content: String,
    val alarmAgree: Boolean
)

data class TargetGroupInfo(
    val startAge: Int?,
    val endAge: Int?,
    val genderType: GenderType,
    val otherCondition: String?,
)

data class ApplyMethodInfo(
    val content: String,
    val formUrl: String?,
    val phoneNum: String?,
)

data class ImageListInfo(
    val images: List<String> = emptyList()
)
