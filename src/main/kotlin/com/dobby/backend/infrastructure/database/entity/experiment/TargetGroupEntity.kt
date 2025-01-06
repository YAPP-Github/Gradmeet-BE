package com.dobby.backend.infrastructure.database.entity.experiment

import com.dobby.backend.infrastructure.database.entity.enum.GenderType
import jakarta.persistence.*

@Entity(name = "target_group")
class TargetGroupEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @Column(name = "start_age")
    val startAge: Int,

    @Column(name = "end_age")
    val endAge: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_type", columnDefinition = "VARCHAR(10)")
    val genderType: GenderType,

    // TODO: other_condition 길이 제한
    @Column(name = "other_condition")
    val otherCondition: String,
) {
}
