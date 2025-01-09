package com.dobby.backend.domain.model.member


data class Researcher(
    val member: Member,
    val univEmail: String,
    val emailVerified: Boolean,
    val univName: String,
    val major: String,
    val labInfo: String? = null
) {
    companion object {
        fun newResearcher(
            member: Member,
            univEmail: String,
            emailVerified: Boolean,
            univName: String,
            major: String,
            labInfo: String?
        ) = Researcher(
            member = member,
            univEmail = univEmail,
            emailVerified = emailVerified,
            univName = univName,
            major = major,
            labInfo = labInfo
        )
    }
}
