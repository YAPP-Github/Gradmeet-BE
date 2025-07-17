package com.dobby.external.prompt.dto

data class ApplyMethodDto(
    val content: String? = null,
    val isFormUrl: Boolean = false,
    val formUrl: String? = null,
    val isPhoneNum: Boolean = false,
    val phoneNum: String? = null
)
