package com.dobby.backend.domain.model.experiment

import com.dobby.backend.infrastructure.database.entity.enums.GenderType

data class TargetGroup(
    val id: Long,
    var startAge: Int?,
    var endAge: Int?,
    var genderType: GenderType,
    var otherCondition: String?
) {

    fun update(startAge: Int?, endAge: Int?, genderType: GenderType?, otherCondition: String?){
        this.startAge = startAge
        this.endAge = endAge
        this.otherCondition = otherCondition
        if (genderType != null) {
            this.genderType = genderType
        }
    }

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
