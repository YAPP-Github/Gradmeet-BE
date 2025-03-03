package com.dobby.database.entity.experiment

import com.dobby.model.experiment.TargetGroup
import com.dobby.enums.member.GenderType
import jakarta.persistence.*

@Entity(name = "target_group")
class TargetGroupEntity(
    @Id
    @Column(name = "target_group_id", columnDefinition = "CHAR(13)")
    val id: String,

    @Column(name = "start_age")
    var startAge: Int?,

    @Column(name = "end_age")
    var endAge: Int?,

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_type", nullable = false)
    var genderType: GenderType,

    @Column(name = "other_condition", length = 300)
    var otherCondition: String?,
) {

    fun toDomain(): TargetGroup = TargetGroup(
        id = id,
        startAge = startAge,
        endAge = endAge,
        genderType = genderType,
        otherCondition = otherCondition
    )

    companion object {
        fun fromDomain(targetGroup: TargetGroup) = with(targetGroup) {
            TargetGroupEntity(
                id = id,
                startAge = startAge,
                endAge = endAge,
                genderType = genderType,
                otherCondition = otherCondition
            )
        }
    }
}
