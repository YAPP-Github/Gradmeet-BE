package com.dobby.backend.domain.model.experiment

data class ApplyMethod(
    val id: Long,
    var phoneNum: String?,
    var formUrl: String?,
    var content: String
) {
    fun update(
        phoneNum: String?, formUrl: String?, content: String?
    ): ApplyMethod {
        return this.copy(
            phoneNum = phoneNum ?: this.phoneNum,
            formUrl = formUrl ?: this.formUrl,
            content = content ?: this.content
        )
    }


    companion object {
        fun newApplyMethod(
            phoneNum: String?,
            formUrl: String?,
            content: String
        ) = ApplyMethod(
            id = 0L,
            phoneNum = phoneNum,
            formUrl = formUrl,
            content = content
        )
    }

}
