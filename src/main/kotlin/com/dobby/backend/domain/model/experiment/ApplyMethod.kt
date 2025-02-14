package com.dobby.backend.domain.model.experiment

data class ApplyMethod(
    val id: String,
    var phoneNum: String?,
    var formUrl: String?,
    var content: String
) {
    fun update(
        phoneNum: String?, formUrl: String?, content: String?
    ): ApplyMethod {
        if(phoneNum == this.phoneNum && formUrl == this.formUrl && content == this.content)
            return this

        return this.copy(
            phoneNum = phoneNum,
            formUrl = formUrl,
            content = content ?: this.content
        )
    }

    companion object {
        fun newApplyMethod(
            id: String,
            phoneNum: String?,
            formUrl: String?,
            content: String
        ) = ApplyMethod(
            id = id,
            phoneNum = phoneNum,
            formUrl = formUrl,
            content = content
        )
    }

}
