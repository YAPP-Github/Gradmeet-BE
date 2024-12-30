package com.dobby.backend.infrastructure.database.entity

import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import java.time.LocalDate

@Entity(name = "researcher")
@DiscriminatorValue("RESEARCHER")
class Researcher (
    @Column(name = "univ_email", length = 100, nullable = false)
    val univEmail : String,

    @Column(name = "email_verified", nullable = false)
    var emailVerified: Boolean = false,

    @Column(name = "univ_name", length = 100, nullable = false)
    val univName : String,

    @Column(name = "major", length = 10, nullable = false)
    val major : String,

    @Column(name = "lab_info", length = 100, nullable = true)
    val labInfo : String,

    id: Long,
    oauthEmail: String,
    provider: ProviderType,
    contactEmail: String,
    name: String,
    birthDate: LocalDate
) : Member(
    id= id,
    oauthEmail = oauthEmail,
    provider = provider,
    contactEmail= contactEmail,
    role = RoleType.RESEARCHER,
    name = name,
    birthDate = birthDate
)
