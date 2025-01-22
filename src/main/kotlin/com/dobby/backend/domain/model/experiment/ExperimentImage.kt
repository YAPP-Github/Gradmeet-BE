package com.dobby.backend.domain.model.experiment

data class ExperimentImage(
    val id: Long,
    val experimentPost: ExperimentPost?,
    val imageUrl: String
) {
    companion object {
        fun newExperimentImage(
            id: Long,
            experimentPost: ExperimentPost,
            imageUrl: String
        ) = ExperimentImage(
            id = id,
            experimentPost = experimentPost,
            imageUrl = imageUrl
        )
    }
}
