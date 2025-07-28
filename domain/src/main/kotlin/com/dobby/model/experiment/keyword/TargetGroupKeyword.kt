package com.dobby.model.experiment.keyword

import com.dobby.enums.member.GenderType

data class TargetGroupKeyword(
    var startAge: Int?,
    var endAge: Int?,
    var genderType: GenderType?,
    var otherCondition: String?
)
