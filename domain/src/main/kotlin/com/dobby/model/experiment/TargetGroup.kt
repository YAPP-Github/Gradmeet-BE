package com.dobby.model.experiment

import com.dobby.enums.member.GenderType

data class TargetGroup(
    val id: String,
    var startAge: Int?,
    var endAge: Int?,
    var genderType: GenderType,
    var otherCondition: String?
) {

    fun update(
        startAge: Int?,
        endAge: Int?,
        genderType: GenderType?,
        otherCondition: String?
    ): TargetGroup {
        if (startAge == this.startAge && endAge == this.endAge && genderType == this.genderType && otherCondition == this.otherCondition) {
            return this
        }

        return this.copy(
            startAge = startAge,
            endAge = endAge,
            otherCondition = otherCondition,
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
