package com.dobby.persistence.entity.member

import com.dobby.model.member.Researcher
import jakarta.persistence.*

@Entity
@Table(name = "researcher")
class ResearcherEntity (
    @Id
    @Column(name= "researcher_id", columnDefinition = "CHAR(13)")
    val id: String,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "member_id", nullable = false)
    val member: MemberEntity,

    @Column(name = "univ_email", length = 100, nullable = false)
    val univEmail : String,

    @Column(name = "email_verified", nullable = false)
    var emailVerified: Boolean = false,

    @Column(name = "univ_name", length = 100, nullable = false)
    val univName : String,

    @Column(name = "major", length = 10, nullable = false)
    val major : String,

    @Column(name = "lab_info", length = 100, nullable = true)
    val labInfo : String?,
) {
    companion object {
        fun fromDomain(researcher: Researcher): ResearcherEntity {
            return ResearcherEntity(
                id = researcher.id,
                member = MemberEntity.fromDomain(researcher.member),
                univEmail = researcher.univEmail,
                emailVerified = researcher.emailVerified,
                univName = researcher.univName,
                major = researcher.major,
                labInfo = researcher.labInfo
            )
        }
    }
    fun toDomain(): Researcher {
        return Researcher(
            id = this.id,
            member = this.member.toDomain(),
            univEmail = this.univEmail,
            emailVerified = this.emailVerified,
            univName = this.univName,
            major = this.major,
            labInfo = this.labInfo,
        )
    }
}
