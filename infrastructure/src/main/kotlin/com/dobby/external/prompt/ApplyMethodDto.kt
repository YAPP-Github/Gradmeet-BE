package com.dobby.external.prompt

data class ApplyMethodDto(
    val content: String?,
    val isFormUrl: Boolean,
    val formUrl: String?,
    val isPhoneNum: Boolean,
    val phoneNum: String?
)
