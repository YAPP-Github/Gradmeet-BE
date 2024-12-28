package com.dobby.backend.infrastructure.database.entity

import com.dobby.backend.infrastructure.database.entity.enums.ProviderType
import com.dobby.backend.infrastructure.database.entity.enums.RoleType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.LocalDate

@Entity(name = "researchers")
class ResearcherEntity (
    @Column(name = "univ_email", length = 100, nullable = false)
    val univEmail : String,

    @Column(name = "email_verified", nullable = false)
    var emailVerified: Boolean = false,

    @Column(name = "univ_name", length = 100, nullable = false)
    val univName : String,

    @Column(name = "major", length = 10, nullable = false)
    val major : String,

    @Column(name = "lab_info", length = 100)
    val labInfo : String?,

    id: Long,
    oauthEmail: String,
    contactEmail : String,
    provider: ProviderType,
    role: RoleType,
    name: String,
    birthDate: LocalDate
) : MemberEntity(id, oauthEmail, provider, role, contactEmail, name, birthDate)

