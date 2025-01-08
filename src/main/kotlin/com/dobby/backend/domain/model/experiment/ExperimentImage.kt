package com.dobby.backend.domain.model.experiment

data class ExperimentImage(
    val id: Long,
    val experimentPostId: Long,
    val imageUrl: String
) {
    companion object {
        fun newExperimentImage(
            id: Long,
            experimentPostId: Long,
            imageUrl: String
        ) = ExperimentImage(
            id = id,
            experimentPostId = experimentPostId,
            imageUrl = imageUrl
        )
    }
}
