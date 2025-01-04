package com.dobby.backend.infrastructure.database.entity

import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import jakarta.persistence.*

@Entity(name = "researcher")
@DiscriminatorValue("RESEARCHER")
class ResearcherEntity (
    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
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
) : MemberEntity(
    id= member.id,
    oauthEmail = member.oauthEmail,
    provider = member.provider,
    role = RoleType.RESEARCHER,
    contactEmail= member.contactEmail,
    name = member.name
)
