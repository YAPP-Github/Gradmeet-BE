package com.dobby.backend.infrastructure.database.entity.experiment

import com.dobby.backend.domain.model.experiment.ExperimentImage
import jakarta.persistence.*

@Entity(name = "experiment_image")
class ExperimentImageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experiment_image_id")
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_post_id")
    var experimentPost: ExperimentPostEntity? = null,

    @Column(name = "image_url", nullable = false)
    val imageUrl: String,
) {
    fun toDomain(): ExperimentImage = ExperimentImage(
        id = id,
        experimentPost = experimentPost?.toDomain(),
        imageUrl = imageUrl
    )

    fun toDomainWithoutPost(): ExperimentImage = ExperimentImage(
        id = id,
        imageUrl = imageUrl,
        experimentPost = null
    )

    companion object {
        fun fromDomain(experimentImage: ExperimentImage): ExperimentImageEntity = with(experimentImage) {
            ExperimentImageEntity(
                id = id,
                experimentPost = experimentPost?.let { ExperimentPostEntity.fromDomain(it) },
                imageUrl = imageUrl
            )
        }
    }
}
