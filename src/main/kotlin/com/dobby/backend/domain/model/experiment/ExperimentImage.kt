package com.dobby.backend.domain.model.experiment

data class ExperimentImage(
    val id: String,
    val experimentPost: ExperimentPost?,
    val imageUrl: String
) {
    companion object {
        fun newExperimentImage(
            id: String,
            experimentPost: ExperimentPost?,
            imageUrl: String
        ) = ExperimentImage(
            id = id,
            experimentPost = experimentPost,
            imageUrl = imageUrl
        )
    }
}
