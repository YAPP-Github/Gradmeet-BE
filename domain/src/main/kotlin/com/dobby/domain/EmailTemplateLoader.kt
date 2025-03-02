package com.dobby.domain

interface EmailTemplateLoader {
    fun loadVerificationTemplate(code: String): String
    fun loadMatchingTemplate(memberName: String, experimentPosts : List<Map<String, String>>, consentDate: String): String
}
