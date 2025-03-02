package com.dobby.backend.presentation.api.dto.request.experiment

import com.dobby.domain.enums.member.GenderType

data class RelationInfo (
    val targetGroupInfo: TargetGroupInfo,
    val applyMethodInfo: ApplyMethodInfo,
    val imageListInfo: ImageListInfo,
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
    val images: List<String>?
)
