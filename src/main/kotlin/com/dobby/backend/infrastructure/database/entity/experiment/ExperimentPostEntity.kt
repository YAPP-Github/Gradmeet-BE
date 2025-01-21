package com.dobby.backend.infrastructure.database.entity.experiment

import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.infrastructure.database.entity.member.MemberEntity
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(name = "experiment_post")
class ExperimentPostEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experiment_post_id")
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: MemberEntity,

    @OneToOne(fetch = FetchType.LAZY,  cascade = [CascadeType.ALL])
    @JoinColumn(name = "target_group_id")
    val targetGroup: TargetGroupEntity,

    @OneToOne(fetch = FetchType.LAZY,  cascade = [CascadeType.ALL])
    @JoinColumn(name = "apply_method_id")
    val applyMethod: ApplyMethodEntity,

    @Column(name = "views")
    var views: Int,

    @Column(name = "title", nullable = false, length = 70)
    val title: String,

    @Column(name = "content", nullable = false, length = 5000)
    val content: String,

    @Column(name = "lead_researcher", nullable = false, length = 150)
    var leadResearcher: String,

    @Column(name = "reward", nullable = false, length = 170)
    val reward: String,

    @Column(name = "start_date")
    val startDate: LocalDate?,

    @Column(name = "end_date")
    val endDate: LocalDate?,

    @Enumerated(EnumType.STRING)
    @Column(name = "time_required")
    val timeRequired: TimeSlot?,

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
    val detailedAddress: String?,

    @Column(name = "alarm_agree", nullable = false)
    val alarmAgree: Boolean,

    @Column(name = "recruit_status", nullable = false)
    var recruitStatus: Boolean,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "experiment_image_id")
    val images: List<ExperimentImageEntity>,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime
) {
    fun toDomain(): ExperimentPost = ExperimentPost(
        id = id,
        member = member.toDomain(),
        targetGroup = targetGroup.toDomain(),
        applyMethod = applyMethod.toDomain(),
        views = views,
        title = title,
        content = content,
        leadResearcher = leadResearcher,
        reward = reward,
        startDate = startDate,
        endDate = endDate,
        timeRequired = timeRequired,
        count = count,
        matchType = matchType,
        univName = univName,
        region = region,
        area = area,
        detailedAddress = detailedAddress,
        alarmAgree = alarmAgree,
        recruitStatus = recruitStatus,
        images = images.map { it.toDomain() },
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(experimentPost: ExperimentPost): ExperimentPostEntity = with(experimentPost) {
            ExperimentPostEntity(
                id = id,
                member = MemberEntity.fromDomain(member),
                targetGroup = TargetGroupEntity.fromDomain(targetGroup),
                applyMethod = ApplyMethodEntity.fromDomain(applyMethod),
                views = views,
                title = title,
                content = content,
                leadResearcher = leadResearcher,
                reward = reward,
                startDate = startDate,
                endDate = endDate,
                timeRequired = timeRequired,
                count = count,
                matchType = matchType,
                univName = univName,
                region = region,
                area = area,
                detailedAddress = detailedAddress,
                alarmAgree = alarmAgree,
                recruitStatus = recruitStatus,
                images = images.map { ExperimentImageEntity.fromDomain(it) },
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
