package com.dobby.external.prompt

data class TargetGroupDto(
    val startAge: Int? = null,
    val endAge: Int? = null,
    val genderType: String? = null,
    val otherCondition: String? = null
)
