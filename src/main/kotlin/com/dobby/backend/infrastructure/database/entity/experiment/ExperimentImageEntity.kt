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
    @JoinColumn(name = "experiment_post_id", nullable = false)
    val experimentPost: ExperimentPostEntity,

    @Column(name = "image_url", nullable = false)
    val imageUrl: String,
) {
    fun toDomain(): ExperimentImage = ExperimentImage(
        id = id,
        experimentPost = experimentPost.toDomain(),
        imageUrl = imageUrl
    )

    companion object {
        fun fromDomain(experimentImage: ExperimentImage): ExperimentImageEntity = with(experimentImage) {
            ExperimentImageEntity(
                id = id,
                experimentPost = ExperimentPostEntity.fromDomain(experimentPost),
                imageUrl = imageUrl
            )
        }
    }
}
