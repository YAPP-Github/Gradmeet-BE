package com.dobby.backend.domain.model.experiment

data class ExperimentImage(
    val id: Long,
    val experimentPost: ExperimentPost?,
    val imageUrl: String
) {
    companion object {
        fun newExperimentImage(
            experimentPost: ExperimentPost?,
            imageUrl: String
        ) = ExperimentImage(
            id = 0L,
            experimentPost = experimentPost,
            imageUrl = imageUrl
        )
    }
}
