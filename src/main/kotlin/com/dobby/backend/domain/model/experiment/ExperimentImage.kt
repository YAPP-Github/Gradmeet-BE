package com.dobby.backend.domain.model.experiment

import com.dobby.backend.util.generateTSID

data class ExperimentImage(
    val id: String,
    val experimentPost: ExperimentPost?,
    val imageUrl: String
) {
    companion object {
        fun newExperimentImage(
            experimentPost: ExperimentPost?,
            imageUrl: String
        ) = ExperimentImage(
            id = generateTSID(),
            experimentPost = experimentPost,
            imageUrl = imageUrl
        )
    }
}
