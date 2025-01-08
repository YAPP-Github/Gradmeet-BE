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
    var views: Int,

    @Column(name = "title", nullable = false, length = 70)
    val title: String,

    @Column(name = "content", nullable = false, length = 5000)
    val content: String,

    @Column(name = "researcher_name", nullable = false, length = 100)
    var researcherName: String,

    @Column(name = "reward", nullable = false, length = 170)
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

    @Column(name = "univ_name", length = 100, nullable = false)
    val univName: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    val region: Region,

    @Enumerated(EnumType.STRING)
    @Column(name = "area", nullable = false)
    val area: Area,

    @Column(name = "detailed_address", length = 70)
    val detailedAddress: String,

    @Column(name = "alarm_agree", nullable = false)
    val alarmAgree: Boolean,

    @OneToMany(mappedBy = "experimentPost", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    val images: List<ExperimentImageEntity> = mutableListOf()
): AuditingEntity() {
}
