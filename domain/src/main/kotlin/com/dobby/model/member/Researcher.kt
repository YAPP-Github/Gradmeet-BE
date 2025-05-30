package com.dobby.model.member

import com.dobby.policy.ResearcherMaskingPolicy
import com.dobby.util.TimeProvider

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
                updatedAt = TimeProvider.currentDateTime()
            ),
            univName = univName,
            major = major,
            labInfo = labInfo
        )
    }

    fun withdraw(): Researcher = this.copy(
        member = member.withdraw(),
        univEmail = ResearcherMaskingPolicy.maskSensitiveData(this.id),
        emailVerified = false,
        univName = ResearcherMaskingPolicy.maskSensitiveData(this.id),
        major = ResearcherMaskingPolicy.maskMajor(),
        labInfo = null
    )
}
