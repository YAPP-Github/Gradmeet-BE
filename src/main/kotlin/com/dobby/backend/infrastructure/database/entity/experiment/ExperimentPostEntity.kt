package com.dobby.backend.infrastructure.database.entity.experiment

import AuditingEntity
import com.dobby.backend.infrastructure.database.entity.member.MemberEntity
import com.dobby.backend.infrastructure.database.entity.enum.MatchType
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import jakarta.persistence.*
import java.time.LocalDate

@Entity(name = "experiment_post")
class ExperimentPostEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val memberId: MemberEntity,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_group_id")
    val targetGroupId: TargetGroupEntity,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apply_method_id")
    val applyMethodId: ApplyMethodEntity,

    @Column(name = "views")
    val views: Int,

    // TODO: title 길이 제한
    @Column(name = "title")
    val title: String,

    // TODO: content 길이 제한
    @Column(name = "content")
    val content: String,

    // TODO: researcherName 길이 제한
    @Column(name = "researcher_name", nullable = false)
    val researcherName: String,

    // TODO: reward 길이 제한
    @Column(name = "reward", nullable = false)
    val reward: String,

    @Column(name = "start_date")
    val startDate: LocalDate,

    @Column(name = "end_date")
    val endDate: LocalDate,

    @Column(name = "duration_minutes")
    val durationMinutes: Int,

    @Column(name = "count", nullable = false)
    val count: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "match_type", nullable = false)
    val matchType: MatchType,

    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    val region: Region,

    @Enumerated(EnumType.STRING)
    @Column(name = "area", nullable = false)
    val area: Area,

    // TODO: detailedAddress 길이 제한
    @Column(name = "detailed_address", nullable = false)
    val detailedAddress: String,

    @Column(name = "alarm_agree", nullable = false)
    val alarmAgree: Boolean,

    @OneToMany(mappedBy = "experimentPost", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    val images: List<ExperimentImageEntity> = mutableListOf()
): AuditingEntity() {
}
