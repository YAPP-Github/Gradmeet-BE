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
    var title: String,

    @Column(name = "content", nullable = false, length = 5000)
    var content: String,

    @Column(name = "lead_researcher", nullable = false, length = 150)
    var leadResearcher: String,

    @Column(name = "reward", nullable = false, length = 170)
    var reward: String,

    @Column(name = "start_date")
    var startDate: LocalDate?,

    @Column(name = "end_date")
    var endDate: LocalDate?,

    @Enumerated(EnumType.STRING)
    @Column(name = "time_required")
    var timeRequired: TimeSlot?,

    @Column(name = "count", nullable = false)
    var count: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "match_type", nullable = false)
    var matchType: MatchType,

    @Column(name = "univ_name", length = 100, nullable = false)
    var univName: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    var region: Region,

    @Enumerated(EnumType.STRING)
    @Column(name = "area", nullable = false)
    var area: Area,

    @Column(name = "detailed_address", length = 70)
    var detailedAddress: String?,

    @Column(name = "alarm_agree", nullable = false)
    var alarmAgree: Boolean,

    @Column(name = "recruit_status", nullable = false)
    var recruitStatus: Boolean = true,

    @OneToMany(mappedBy = "experimentPost", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val _images: MutableList<ExperimentImageEntity> = mutableListOf(),

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime
) {
    val images: List<ExperimentImageEntity>
        get() = _images.toList()

    fun addImage(image: ExperimentImageEntity) {
        require(image.imageUrl.isNotBlank())
        image.experimentPost = this
        _images.add(image)
    }


    fun removeImage(image: ExperimentImageEntity) {
        val removed = _images.removeIf {
            val shouldRemove = it.id == image.id
            if (shouldRemove) it.experimentPost = null
            shouldRemove
        }
        require(removed)
    }

    fun updateImageList(newImages: List<ExperimentImageEntity>) {
        val newIds = newImages.map { it.id }.toSet()
        val existingIds = _images.map { it.id }.toSet()

        newImages.filter { it.id !in existingIds }
            .forEach { addImage(it) }
        _images.removeIf { it.id !in newIds }
    }

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
        images = _images.map { it.toDomainWithoutPost() }.toMutableList(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(experimentPost: ExperimentPost): ExperimentPostEntity {
            val entity = ExperimentPostEntity(
                id = experimentPost.id,
                member = MemberEntity.fromDomain(experimentPost.member),
                targetGroup = TargetGroupEntity.fromDomain(experimentPost.targetGroup),
                applyMethod = ApplyMethodEntity.fromDomain(experimentPost.applyMethod),
                views = experimentPost.views,
                title = experimentPost.title,
                content = experimentPost.content,
                leadResearcher = experimentPost.leadResearcher,
                reward = experimentPost.reward,
                startDate = experimentPost.startDate,
                endDate = experimentPost.endDate,
                timeRequired = experimentPost.timeRequired,
                count = experimentPost.count,
                matchType = experimentPost.matchType,
                univName = experimentPost.univName,
                region = experimentPost.region,
                area = experimentPost.area,
                detailedAddress = experimentPost.detailedAddress,
                alarmAgree = experimentPost.alarmAgree,
                recruitStatus = experimentPost.recruitStatus,
                createdAt = experimentPost.createdAt,
                updatedAt = experimentPost.updatedAt
            )
            experimentPost.images.forEach { image ->
                entity.addImage(ExperimentImageEntity.fromDomain(image))
            }
            return entity
        }
    }
}
