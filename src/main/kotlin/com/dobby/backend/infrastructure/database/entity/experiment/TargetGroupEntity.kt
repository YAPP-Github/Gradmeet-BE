package com.dobby.backend.infrastructure.database.entity.experiment

import com.dobby.backend.domain.model.experiment.TargetGroup
import com.dobby.backend.infrastructure.database.entity.enum.GenderType
import jakarta.persistence.*

@Entity(name = "target_group")
class TargetGroupEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "target_group_id")
    val id: Long,

    @Column(name = "start_age")
    val startAge: Int?,

    @Column(name = "end_age")
    val endAge: Int?,

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_type", nullable = false)
    val genderType: GenderType,

    @Column(name = "other_condition", length = 300)
    val otherCondition: String?,
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
