package com.dobby.model.experiment

import com.dobby.enums.member.GenderType
import com.dobby.exception.ExperimentPostTargetGroupAgeException

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

        validateAgeRange(startAge, endAge)

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
        ): TargetGroup {
            validateAgeRange(startAge, endAge)
            return TargetGroup(
                id = id,
                startAge = startAge,
                endAge = endAge,
                genderType = genderType,
                otherCondition = otherCondition
            )
        }

        private fun validateAgeRange(startAge: Int?, endAge: Int?) {
            if (startAge != null && startAge < 0) throw ExperimentPostTargetGroupAgeException
            if (endAge != null && endAge < 0) throw ExperimentPostTargetGroupAgeException
            if (startAge != null && endAge != null && endAge < startAge) throw ExperimentPostTargetGroupAgeException
        }
    }
}
