package com.dobby.backend.domain.model.experiment

import com.dobby.backend.infrastructure.database.entity.enums.GenderType

data class TargetGroup(
    val id: String,
    var startAge: Int?,
    var endAge: Int?,
    var genderType: GenderType,
    var otherCondition: String?
) {

    fun update(
        startAge: Int?, endAge: Int?, genderType: GenderType?, otherCondition: String?): TargetGroup{
        return this.copy(
            startAge = startAge ?: this.startAge,
            endAge = endAge ?: this.endAge,
            otherCondition = otherCondition ?: this.otherCondition,
            genderType = genderType ?: this.genderType
        )
    }

    companion object {
        fun newTargetGroup(
            id: String,
            startAge: Int?,
            endAge: Int?,
            genderType: GenderType,
            otherCondition: String?
        ) = TargetGroup(
            id = id,
            startAge = startAge,
            endAge = endAge,
            genderType = genderType,
            otherCondition = otherCondition
        )
    }
}
