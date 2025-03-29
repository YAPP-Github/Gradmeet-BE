package com.dobby.persistence.entity.experiment

import com.dobby.model.experiment.ExperimentImage
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity(name = "experiment_image")
class ExperimentImageEntity(
    @Id
    @Column(name = "experiment_image_id", columnDefinition = "CHAR(13)")
    val id: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_post_id")
    var experimentPost: ExperimentPostEntity? = null,

    @Column(name = "image_url", nullable = false)
    val imageUrl: String
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
