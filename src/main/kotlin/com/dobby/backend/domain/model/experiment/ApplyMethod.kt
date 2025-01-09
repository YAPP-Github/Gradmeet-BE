package com.dobby.backend.domain.model.experiment

data class ApplyMethod(
    val id: Long,
    val phoneNum: String,
    val formUrl: String,
    val content: String
) {
    companion object {
        fun newApplyMethod(
            id: Long,
            phoneNum: String,
            formUrl: String,
            content: String
        ) = ApplyMethod(
            id = id,
            phoneNum = phoneNum,
            formUrl = formUrl,
            content = content
        )
    }
}
