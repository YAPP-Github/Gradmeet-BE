package com.dobby.backend.domain.model.experiment

import com.dobby.backend.infrastructure.database.entity.enum.GenderType

data class TargetGroup(
    val id: Long,
    val startAge: Int,
    val endAge: Int,
    val genderType: GenderType,
    val otherCondition: String
) {
    companion object {
        fun newTargetGroup(
            id: Long,
            startAge: Int,
            endAge: Int,
            genderType: GenderType,
            otherCondition: String
        ) = TargetGroup(
            id = id,
            startAge = startAge,
            endAge = endAge,
            genderType = genderType,
            otherCondition = otherCondition
        )
    }
}
