package com.dobby.backend.infrastructure.database.entity.experiment

import AuditingEntity
import jakarta.persistence.*

@Entity(name = "experiment_image")
class ExperimentImageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_post_id", referencedColumnName = "id", nullable = false)
    val experimentPost: ExperimentPostEntity,

    @Column(name = "image_url", nullable = false)
    val imageUrl: String,
) : AuditingEntity() {
}
