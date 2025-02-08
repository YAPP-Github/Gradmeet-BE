package com.dobby.backend.domain.model.member

import java.time.LocalDateTime

data class Researcher(
    val id: String,
    var member: Member,
    val univEmail: String,
    val emailVerified: Boolean,
    val univName: String,
    val major: String,
    val labInfo: String? = null
) {

    companion object {
        fun newResearcher(
            id: String,
            member: Member,
            univEmail: String,
            emailVerified: Boolean,
            univName: String,
            major: String,
            labInfo: String?
        ) = Researcher(
            id = id,
            member = member,
            univEmail = univEmail,
            emailVerified = emailVerified,
            univName = univName,
            major = major,
            labInfo = labInfo
        )
    }

    fun updateInfo(
        contactEmail: String,
        name: String,
        univName: String,
        major: String,
        labInfo: String?
    ): Researcher {
        return this.copy(
            member = member.copy(
                contactEmail = contactEmail,
                name = name,
                updatedAt = LocalDateTime.now()
            ),
            univName = univName,
            major = major,
            labInfo = labInfo
        )
    }

    fun withdraw(): Researcher = this.copy(
        member = member.withdraw(),
        univEmail = "",
        univName = "",
        major = "",
        labInfo = null
    )
}
